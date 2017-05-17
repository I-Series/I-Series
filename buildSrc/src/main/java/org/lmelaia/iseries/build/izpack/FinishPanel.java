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
 * A wrapper for the installers finish panel.
 * 
 * @author Luke Melaia
 */
public class FinishPanel {
    
    private final FinishPanelType type;
    
    /**
     * 
     * @param type The variation of finish panel to use.
     */
    public FinishPanel(FinishPanelType type){
        this.type = Objects.requireNonNull(type);
    }
    
    /**
     * Constructs a finish panel with the
     * {@link FinishPanelType#SIMPLE_FINISH_PANEL} variation.
     */
    public FinishPanel(){
        this(FinishPanelType.SIMPLE_FINISH_PANEL);
    }

    public FinishPanelType getType() {
        return type;
    }
    
    /**
     * The finish panels variations.
     */
    public enum FinishPanelType{
        SIMPLE_FINISH_PANEL,
        FINISH_PANEL;
    }
}
