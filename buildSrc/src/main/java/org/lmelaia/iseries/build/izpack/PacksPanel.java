/*   Copyright (C) 2016  Luke Melaia
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lmelaia.iseries.build.izpack;

import java.util.Objects;

/**
 *
 * @author Luke Melaia
 */
public class PacksPanel {
    
    private final PacksPanelType type;
    
    public PacksPanel(PacksPanelType type){
        this.type = Objects.requireNonNull(type);
    }
    
    public PacksPanel(){
        this(PacksPanelType.PACKS_PANEL);
    }

    public PacksPanelType getType() {
        return type;
    }
    
    public enum PacksPanelType{
        PACKS_PANEL,
        IMAGE_PACKS_PANEL,
        TREE_PACKS_PANEL;
    }
}
