/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Vladimir Mikhailov <beykerykt@gmail.com>
 * Copyright (c) 2020 Qveshn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package me.eccentric_nz.tardischunkgenerator.light;

import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.*;

public class RequestSteamMachine implements Runnable {

    private final Queue<Runnable> REQUEST_QUEUE = new ConcurrentLinkedQueue<>();
    private final Map<ChunkLocation, ChunkUpdateInfo> chunksToUpdate = new HashMap<>();
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private boolean isStarted;
    private int maxIterationsPerTick;
    // THREADS
    private ScheduledFuture<?> sch;

    public void start(int ticks, int maxIterationsPerTick) {
        if (!isStarted) {
            this.maxIterationsPerTick = maxIterationsPerTick;
            sch = executor.scheduleWithFixedDelay(this, 0, 50 * ticks, TimeUnit.MILLISECONDS);
            isStarted = true;
        }
    }

    public void shutdown() {
        if (isStarted) {
            REQUEST_QUEUE.clear();
            maxIterationsPerTick = 0;
            sch.cancel(false);
            isStarted = false;
        }
    }

    public boolean isStarted() {
        return isStarted;
    }

    public boolean addToQueue(Runnable request) {
        if (request != null) {
            REQUEST_QUEUE.add(request);
            return true;
        }
        return false;
    }

    public void addChunkToUpdate(ChunkInfo info, LightType lightType, Collection<? extends Player> receivers) {
        int SectionY = info.getChunkY();
        INMSHandler nmsHandler = new NMSHandler();
        if (nmsHandler.isValidSectionY(SectionY)) {
            ChunkLocation chunk = new ChunkLocation(info.getWorld(), info.getChunkX(), info.getChunkZ());
            int sectionYMask = nmsHandler.asSectionMask(SectionY);
            Collection<Player> players = new ArrayList<>(receivers != null ? receivers : info.getReceivers());
            addToQueue(() -> {
                ChunkUpdateInfo chunkUpdateInfo = chunksToUpdate.get(chunk);
                if (chunkUpdateInfo == null) {
                    chunksToUpdate.put(chunk, chunkUpdateInfo = new ChunkUpdateInfo());
                }
                chunkUpdateInfo.add(lightType, sectionYMask, players);
            });
        }
    }

    @Override
    public void run() {
        try {
            int iterationsCount = 0;
            Runnable request;
            while (iterationsCount < maxIterationsPerTick && (request = REQUEST_QUEUE.poll()) != null) {
                request.run();
                iterationsCount++;
            }
            INMSHandler nmsHandler = new NMSHandler();
            for (Map.Entry<ChunkLocation, ChunkUpdateInfo> item : chunksToUpdate.entrySet()) {
                ChunkLocation chunk = item.getKey();
                ChunkUpdateInfo chunkUpdateInfo = item.getValue();
                int sectionMaskSky = chunkUpdateInfo.getSectionMaskSky();
                int sectionMaskBlock = chunkUpdateInfo.getSectionMaskBlock();
                Collection<? extends Player> players = nmsHandler.filterVisiblePlayers(chunk.getWorld(), chunk.getX(), chunk.getZ(), chunkUpdateInfo.getPlayers());
                nmsHandler.sendChunkSectionsUpdate(chunk.getWorld(), chunk.getX(), chunk.getZ(), sectionMaskSky, sectionMaskBlock, players);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            chunksToUpdate.clear();
        }
    }
}
