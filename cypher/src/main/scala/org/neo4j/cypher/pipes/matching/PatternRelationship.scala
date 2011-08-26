/**
 * Copyright (c) 2002-2011 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.cypher.pipes.matching

import org.neo4j.graphdb.{DynamicRelationshipType, Node, Relationship, Direction}

class PatternRelationship(key: String, leftNode: PatternNode, rightNode: PatternNode, relType: Option[String], dir: Direction)
  extends PatternElement(key)
  with PinnablePatternElement[Relationship] {

  def getOtherNode(node: PatternNode) = if(leftNode==node) rightNode else leftNode
  def getRealRelationships(node: PatternNode, realNode: Node): java.lang.Iterable[Relationship] = {
    relType match {
      case Some(typeName) => realNode.getRelationships(getDirection(node), DynamicRelationshipType.withName(typeName))
      case None => realNode.getRelationships(getDirection(node))
    }
  }
  private def getDirection(node: PatternNode): Direction = {
    dir match {
      case Direction.OUTGOING => if (node == leftNode) Direction.OUTGOING else Direction.INCOMING
      case Direction.INCOMING => if (node == rightNode) Direction.OUTGOING else Direction.INCOMING
      case Direction.BOTH => Direction.BOTH
    }
  }

}
