package kaiju.mapgen

import kaiju.mapgen.noise.NoiseFunction3d
import kaiju.mapgen.three.brush.drawCell
import kaiju.mapgen.three.fill
import kaiju.mapgen.three.fillWithBorder
import kaiju.mapgen.three.floodFill
import kaiju.mapgen.three.predicate.*
import kaiju.math.*
import kaiju.math.geom.RectangularPrism
import kaiju.pathfinder.findPath3d
import org.junit.Test
import kotlin.math.cos
import kotlin.math.sin


class GenerateMap3dTest {
    @Test
    fun testGenerationOfBoxViaPredicates() {
        val em1 = matrix3dOf(10, 10, 10) { x, y, z -> unknown }
        fillWithBorder(em1, floor, wall)

        val em2 = matrix3dOf(10, 10, 10) { x, y, z -> unknown }
        fill(em2, cellNearEdge(), drawCell(wall))
        fill(em2, not(cellNearEdge()), drawCell(floor))

        testEquality(em1, em2)
        println("BOX 1 - borderfill")
        printMap(em1)
        println("Box 2 - Fill with CellNearEdge and notPredicates")
        printMap(em2)
    }

    @Test
    fun testGenerationOfTreesViaPredicates() {
        val em = matrix3dOf(20, 20, 20) { x, y, z -> unknown }
        fill(em, cellNearEdge(), drawCell(wall))
        fill(em, not(cellNearEdge()), drawCell(floor))
        println("Gen trees 1 - not planted yet")
        printMap(em)
        val vegetation = NoiseFunction3d(10.0, 30.0, 20.0, 16.0, 13.0, 15.0)
        val tree = ExampleCellType('T', true)
        fill(
            em,
            and(
                noiseGreaterThan(vegetation, 0.5),
                cellEquals(floor)
            ),
            drawCell(tree)
        )

        val snow = NoiseFunction3d(-10.0, -500.0, 20.0, 18.0, 20.0, 22.0)
        val pineTree = ExampleCellType('p', true)
        fill(
            em, and(
                noiseGreaterThan(snow, 0.6),
                cellEquals(tree)
            ), drawCell(pineTree)
        )
        println("Gen trees 2 - you should start seeing T's")
        printMap(em)
    }

    @Test
    fun testOr() {
        val em = matrix3dOf(10, 10, 10) { x, y, z -> grass }
        em[5, 5, 0] = floor

        fill(
            em,
            or(
                cellNearEdge(),
                nearCell(floor)
            ),
            drawCell(wall)
        )
        printMap(em)
    }

    @Test
    fun testFloodFill() {
        val em = matrix3dOf(10, 10, 10) { x, y, z -> unknown }
        val center = Vec3[5, 5, 5]
        val wallPoints: List<Vec3> = center.getMooreNeighborhood()
        assert(wallPoints.size == 26)

        //Draw a wall
        fill(em,
            { map: Matrix3d<ExampleCellType>, x: Int, y: Int, z: Int -> wallPoints.contains(Vec3[x, y, z]) },
            { m, x, y, z -> m[x, y, z] = wall }
        )
        assert(em[5, 5, 5] === unknown)
        for (wallPoint in wallPoints) {
            assert(em[wallPoint] === wall)
        }


        // Floodfill inside that wall and make sure it doesnt escape
        floodFill(em, center, cellEquals(unknown), drawCell(floor))
        assert(em[center] === floor)
        for (wallPoint in wallPoints) {
            assert(em[wallPoint] === wall)
        }
        assert(em[0, 0, 0] === unknown)
        printMap(em)
    }

    @Test
    fun makeAVillageByALake() {
        val map = matrix3dOf(100, 100, 10) { x, y, z -> unknown }
        fillWithBorder(map, grass, forest)
        val xMid: Int = map.xSize / 2
        val yMid: Int = map.ySize / 2
        val buildingRadius = xMid / 2.toDouble()
        val lakeRadius = buildingRadius / 2
        val forestRadius = buildingRadius + 1
        val rooms: List<RectangularPrism> = (0..10).map { i: Int ->
            val angle = getDoubleInRange(0.0, Math.PI * 2)
            val x: Int = xMid + (buildingRadius * cos(angle)).toInt() + getIntInRange(-3, 3)
            val y: Int = yMid + (buildingRadius * sin(angle)).toInt() + getIntInRange(-3, 3)
            val xw: Int = getIntInRange(2, 3)
            val yw: Int = getIntInRange(2, 3)
            RectangularPrism(Vec3[x - xw, y - yw, 0], Vec3[x + xw, y + yw, 1])
        }
        rooms.forEach { room ->
            fill(
                map,
                containedIn(room),
                drawCell(floor)
            )
        }

        //Put walls around buildings
        fill(
            map,
            and(
                nearCell(floor),
                cellEquals(grass)
            ),
            drawCell(wall)
        )

        // Fill in pond
        val lake = NoiseFunction3d(30.0, 20.0, -234.0, 24.0, 24.0, 24.0)
        fill(map,
            and(
                cellEquals(grass),
                { e: Matrix3d<ExampleCellType>, x: Int, y: Int, z: Int ->
                    0.1 * lake.gen(
                        x.toDouble(),
                        y.toDouble(),
                        z.toDouble()
                    ) + 0.9 * ((lakeRadius - getEuclideanDistance(
                        xMid.toDouble(),
                        yMid.toDouble(),
                        x.toDouble(),
                        y.toDouble()
                    )) / xMid.toFloat()) > 0
                }
            ),
            drawCell(water)
        )
        val trees = NoiseFunction3d(10.0, 30.0, 20.0, 4.0, 4.0, 4.0)
        fill(map,
            and(
                cellEquals(grass),
                { e, x, y, z ->
                    trees.gen(
                        x.toDouble(),
                        y.toDouble(),
                        z.toDouble()
                    ) + (forestRadius - getEuclideanDistance(
                        xMid.toDouble(),
                        yMid.toDouble(),
                        x.toDouble(),
                        y.toDouble()
                    )) / xMid.toFloat() < 0
                }
            ),
            drawCell(forest)
        )

        // Dig paths
        val centers: List<Vec3> = rooms.map { room -> room.center() }
        var centerLast = Vec3[xMid, 0, 0]
        for (center in centers) {

            val path: List<Vec3>? = findPath3d(
                map.getSize(),
                { v ->
                    when (map[v]) {
                        wall -> 10.0
                        floor -> 2.0
                        grass -> 1.0
                        forest -> 2.0
                        water -> 30.0
                        else -> 1.0
                    }
                },
                ::getEuclideanDistance,
                { it.vonNeumanNeighborhood() },
                centerLast, center
            )
            centerLast = center


            path?.forEach { it: Vec3 ->
                val cellType: ExampleCellType = map[it]
                map[it] = if (cellType === grass || cellType === forest) {
                    grass
                } else if (cellType === wall || cellType === door) {
                    door
                } else {
                    floor
                }
            }
            //            }

//            fill(em, (e, x, y) -> false,
//                    (e, x, y) -> {
//                    }
//            );
        }
        printMap(map)
    }

    private fun <T> testEquality(map1: Matrix3d<T>, map2: Matrix3d<T>) {
        assert(map1.getSize() == map2.getSize())

        for (y in 0 until map1.xSize) {
            for (x in 0 until map1.ySize) {
                for (z in 0 until map1.zSize) {
                    assert(map1[x, y, z] === map2[x, y, z])
                }
            }
        }
    }

    private fun printMap(exampleGeneratedMap3d: Matrix3d<ExampleCellType>) {
        for (y in 0 until exampleGeneratedMap3d.xSize) {
            for (x in 0 until exampleGeneratedMap3d.ySize) {
                for (z in 0 until exampleGeneratedMap3d.zSize) {
                    print(exampleGeneratedMap3d[x, y, z].glyph)
                }
            }
            println()
        }
    }

    companion object {
        val unknown: ExampleCellType = ExampleCellType('?', true)
        val wall: ExampleCellType = ExampleCellType('X', true)
        val floor: ExampleCellType = ExampleCellType('.', false)
        val grass: ExampleCellType = ExampleCellType(',', false)
        val forest: ExampleCellType = ExampleCellType('T', true)
        val water: ExampleCellType = ExampleCellType('w', true)
        val door: ExampleCellType = ExampleCellType('D', false)
    }
}

