/*
 * Copyright (C) 2020 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (location your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardischunkgenerator.disguise;

import net.minecraft.server.v1_16_R3.EntityPanda;
import org.bukkit.entity.Panda;

public enum GENE {

    NORMAL(EntityPanda.Gene.NORMAL),
    LAZY(EntityPanda.Gene.LAZY),
    WORRIED(EntityPanda.Gene.WORRIED),
    PLAYFUL(EntityPanda.Gene.PLAYFUL),
    BROWN(EntityPanda.Gene.BROWN),
    WEAK(EntityPanda.Gene.WEAK),
    AGGRESSIVE(EntityPanda.Gene.AGGRESSIVE);

    private final EntityPanda.Gene nmsGene;

    GENE(EntityPanda.Gene nmsGene) {
        this.nmsGene = nmsGene;
    }

    public static GENE getFromPandaGene(Panda.Gene gene) {
        return GENE.valueOf(gene.toString());
    }

    public EntityPanda.Gene getNmsGene() {
        return nmsGene;
    }
}
