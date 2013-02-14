/**
 * Femulator - MIDI Mapper and F1 Emulator control for Traktor
 * Copyright 2013, Andrew Bythell <abythell@ieee.org>
 * http://angryelectron.com/femulator
 *
 * Femulator is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 * 
 * Femulator is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Femulator.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.angryelectron.femulator.mapviewer;

import com.angryelectron.libf1.F1Group;
import com.angryelectron.femulator.f1api.F1RefreshEvent;
import com.angryelectron.femulator.f1api.F1Service;
import com.angryelectron.femulator.f1api.F1Utils;
import java.util.Collection;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;

class GroupChildFactory extends ChildFactory<F1Group> {
        
    private F1Service f1;       
    private Result<F1RefreshEvent> result;    
    
    /**
     * This allows the listener to be attached in the constructor.  If the class
     * implements LookupListener, the constructor leaks.  When new F1MapEntries
     * are added to the F1Service Lookup, refresh the list of nodes.  This will
     * keep the Node List in the TopComponent up-to-date with the F1Service model.
     */
    private LookupListener listener = new LookupListener() {
        @Override
        public void resultChanged(LookupEvent ev) {
            Collection<? extends F1RefreshEvent> allInstances = result.allInstances();            
            if (!allInstances.isEmpty()) {            
                refresh(true);            
            }
        }        
    };    
    
    /**
     * Constructor.  Start listening to F1Service's lookup.
     */
    public GroupChildFactory() {
        f1 = F1Utils.getF1Service();
        result = f1.getLookup().lookupResult(F1RefreshEvent.class);
        result.addLookupListener(listener);         
    }
    

    /**
     * Create keys used to populate nodes from the list of entries.
     * @param toPopulate Nodes will be created for all items added to this list.
     * @return true
     */
    @Override
    protected boolean createKeys(List<F1Group> toPopulate) {                
        toPopulate.addAll(f1.getDevice().listGroups());        
        return true;
    }

    /**
     * Create a new node
     * @param key An object, taken from the toPopulate list created by createKeys().
     * @return A new node.
     */
    @Override
    protected Node createNodeForKey(F1Group key) {
        //Node result = new AbstractNode(Children.create(new GroupChildFactory(), true), Lookups.singleton(key));
        return new GroupNode(key);
    }
    
}
