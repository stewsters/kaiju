package kai.pathfinder

import kai.math.InverseRectangle
import kai.math.Matrix2d
import kai.math.Obstacle
import kai.math.Rectangle
import kai.math.Vec2
import org.junit.Test

class PathfinderTest {

    @Test
    fun testPathfindingInOpen2d() {

        // If blocks are 6.5 * 6.5 inches
        // xSize = 50
        // ySize = 100

        val size = Vec2[20, 30]
        val safeDist = 3

        // We should take in a list of autonomous "safe zones" for our allies too
        val obstacles = listOf<Obstacle>(
                Rectangle(Vec2[5, 5], Vec2[15, 10]), // switch
                Rectangle(Vec2[5, 15], Vec2[15, 20]), // scale
                InverseRectangle(Vec2[0, 0], size) // playing field
        )

        // This represents the cost to travel as a scalar field.  Keeping ourselves away from the edges
        // is important, since we are not necessarily accurate, and a default path finder will
        // run you as close to the edge as is mathematically possible.
        val fieldMap = Matrix2d<Double>(size.x, size.y, { x, y ->

            val d: Double = obstacles.map { it.minDist(Vec2[x, y]) }.min()!!
            if (d <= 0) {
                Double.MAX_VALUE
            } else if (d > safeDist) {
                1.0
            } else {
                // weight spaces near obstacles higher.
                2 * (1 - (d / safeDist)) + 1
            }
        })

        val start = Vec2(1, 1)
        val end = Vec2(19, 29)

        val path = findPath2d(fieldMap, { it.mooreNeighborhood() }, start, end)


        assert(path != null)
        assert(path?.first() == start)
        assert(path?.last() == end)

    }


//    @Test
//    fun testPathMakerWithTurns() {
//
////        val bi = ImageIO.read(File("${System.getProperty("user.home")}${File.separator}Desktop${File.separator}pathing${File.separator}editMap.png"))
//        val obstacles = mutableListOf<Obstacle>()
//
////        for (x in 0 until bi.getWidth()) {
////            for (y in 0 until bi.getHeight()) {
////                if (Color(bi.getRGB(x, bi.getHeight()-y -1)).red < 10) {
////                    obstacles.add(Rectangle(Vec2[x, y], Vec2[x, y]))
////                }
////            }
////        }
//
//        StartingPos.values().forEach { start ->
//            EndingPos.values().forEach { end ->
//
//                val path = makePath(start, end, obstacles)
//
//                if (path == null) {
//                    println("No possible path, do a default action instead")
//                }
//
//                if (end == EndingPos.RIGHT_SWITCH) {
////                    assert(path == null)
//                } else {
////                    assert(path != null)
////                    assert(path?.first() == Vec3(start.pos.x, start.pos.y, start.facing.ordinal))
//  //                  assert(path?.last() == Vec3(end.pos.x, end.pos.y, end.facing.ordinal))
//                }
////                println("start ${start} end ${end}")
////                path?.forEach { println("x: ${it.x} y: ${it.y} ${Facing.values()[it.z]}") }
//
//                drawPathImage(path, obstacles, "${start.name}-${end.name}.png")
//
//            }
//        }
//    }

//    @Test
//    fun generateMap() {
//        drawPathImage(path = null, obstacles = arrayListOf(), name = "blankMap.png")
//    }

//    private fun drawPathImage(path: List<Vec3>?, obstacles: List<Obstacle>, name: String) {
//
//        val out = BufferedImage(FieldMeasurement.pathfinderBlocksWidth, FieldMeasurement.pathfinderBlocksLength, TYPE_INT_RGB)
//
//        val map = createMap(obstacles)
//
//        for (x in 0 until out.width) {
//            for (y in 0 until out.height) {
//
//                val ammt = map[x, y].toFloat()
//
//                if (ammt > 10) {
//                    out.setRGB(x, out.height - y - 1, Color.BLACK.rgb)
//                } else {
//                    out.setRGB(x, out.height - y - 1, Color(1 - ammt / 10f, 1 - ammt / 10f, 1 - ammt / 10f).rgb)
//                }
//            }
//        }
//
//        val colors = listOf(Color.RED, Color.GREEN, Color.MAGENTA, Color.CYAN)
//        path?.forEach {
//            out.setRGB(it.x, out.height - it.y - 1, colors[it.z].rgb)
//        }
////        out.graphics.
//
//        val plans = File("build/plans")
//        plans.mkdirs()
//        ImageIO.write(out, "PNG", File(plans, name))
//    }


    private fun printField(fieldMap: Matrix2d<Double>, path: List<Vec2>? = null) {
        for (ym in 1..fieldMap.ySize) {
            val y = fieldMap.ySize - ym
            for (x in 0 until fieldMap.xSize) {
                val value = Math.min(Math.round(fieldMap[x, y]), 9)

                if (path?.contains(Vec2[x, y]) == true) {
                    print("X ")
                } else {
                    print(value.toString() + " ")
                }
            }
            println()
        }

    }
}