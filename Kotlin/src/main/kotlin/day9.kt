fun day9 (lines: List<String>) {
    day9part1(lines[0])
    day9part2()
}

fun day9part1(line: String) {
    val diskMap = mutableListOf<File>()
    var emptySpace = false
    var fileId = 0L

    line.forEachIndexed { index, char ->
        for (i in 0 until char.code - 48) {
            if (emptySpace) {
                diskMap.add(File(-1))
            } else {
                diskMap.add(File(fileId))
            }
        }
        
        if (emptySpace) {
            fileId++
        }
        
        emptySpace = !emptySpace
    }
    
    val compactedFileSystem = createCompactedFileSystem(diskMap)
    
    var checkSum = 0L
    
    compactedFileSystem.forEachIndexed { index, file -> 
        checkSum += (index * file.fileId)
    }
    
    println("Day 9 part 1: $checkSum")
}

fun day9part2() {

}

fun createCompactedFileSystem(diskMap: List<File>): List<File> {
    val compactedFileSystem = mutableListOf<File>()
    var endOfFile = diskMap.indices.last

    diskMap.forEachIndexed { index, file ->
        if (file.fileId >= 0) {
            compactedFileSystem.add(file)
            return@forEachIndexed
        }

        if (endOfFile < 0) {
            return@forEachIndexed
        }

        while(diskMap[endOfFile].fileId == -1L) {
            endOfFile--
            if (endOfFile < 0) {
                return@forEachIndexed
            }
        }

        if (index > endOfFile) {
            return compactedFileSystem
        }

        compactedFileSystem.add(diskMap[endOfFile])
        endOfFile--
    }
    
    return compactedFileSystem
}

data class File (val fileId: Long)