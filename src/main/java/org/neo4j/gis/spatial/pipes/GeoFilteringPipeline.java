/**
 * Copyright (c) 2002-2011 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.gis.spatial.pipes;

import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.pipes.filter.FilterAttributes;
import org.neo4j.gis.spatial.pipes.filter.FilterBoundingBox;
import org.neo4j.gis.spatial.pipes.filter.FilterCQL;

import com.tinkerpop.pipes.filter.FilterPipe;
import com.tinkerpop.pipes.util.FluentPipeline;

public class GeoFilteringPipeline<S, E> extends FluentPipeline<S, E>
{

    protected final Layer layer;

    public GeoFilteringPipeline( Layer layer )
    {
        this.layer = layer;
    }

    public GeoFilteringPipeline<SpatialDatabaseRecord, SpatialDatabaseRecord> all()
    {
        return (GeoFilteringPipeline<SpatialDatabaseRecord, SpatialDatabaseRecord>) this.add(new SearchAllPipe(layer));
    }
    
    public GeoFilteringPipeline<SpatialDatabaseRecord, SpatialDatabaseRecord> attributes(String key, String value, FilterPipe.Filter filter)
    {
        return (GeoFilteringPipeline<SpatialDatabaseRecord, SpatialDatabaseRecord>) this.add(new FilterAttributes(layer, key, value, filter));
    }
    
    public GeoFilteringPipeline<SpatialDatabaseRecord, SpatialDatabaseRecord> bbox(double minLon, double minLat, double maxLon, double maxLat)
    {
        return (GeoFilteringPipeline<SpatialDatabaseRecord, SpatialDatabaseRecord>) this.add(new FilterBoundingBox<S, E>(layer, minLon, minLat, maxLon, maxLat));
    }

    public GeoFilteringPipeline<SpatialDatabaseRecord, SpatialDatabaseRecord> cql(String cql)
    {
        return (GeoFilteringPipeline<SpatialDatabaseRecord, SpatialDatabaseRecord>) this.add(new FilterCQL<S, E>(layer, cql));
    }

    
    public GeoProcessingPipeline<SpatialDatabaseRecord, SpatialDatabaseRecord> process() {
        return (GeoProcessingPipeline<SpatialDatabaseRecord, SpatialDatabaseRecord>) this.layer.process().start( this );
    }

}