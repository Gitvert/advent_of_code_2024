import jdk.javadoc.internal.doclets.formats.html.markup.HtmlStyle.blockList

fun day9 (lines: List<String>) {
    day9part1(lines[0])
    day9part2(lines[0])
}

fun day9part1(line: String) {
    val diskMap = mutableListOf<File>()
    var emptySpace = false
    var fileId = 0

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

        while(diskMap[endOfFile].fileId == -1) {
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

fun day9part2(line: String) {
    val diskMap = mutableListOf<Part2File>()
    var emptySpace = false
    var fileId = 0
    var blockIndex = 0

    line.forEachIndexed { index, char ->
        if (emptySpace) {
            if (char != '0') {
                diskMap.add(Part2File(-1, blockIndex, blockIndex + char.code - 48 - 1, char.code - 48))
            }
            fileId++
        } else {
            if (char != '0') {
                diskMap.add(Part2File(fileId, blockIndex, blockIndex + char.code - 48 - 1, char.code - 48))
            }
        }

        blockIndex += char.code - 48
        emptySpace = !emptySpace
    }

    createNonFragmentedCompactedFileSystem(diskMap)
    
    val blockList = convertToBlockList(diskMap.filter { it.size > 0 })
    
    var checksum = 0L

    blockList.forEachIndexed { index, file ->
        if (file.fileId >= 0) {
            checksum += (index * file.fileId)
            
        }
    }

    println("Day 9 part 2: $checksum")
}

fun createNonFragmentedCompactedFileSystem(diskMap: MutableList<Part2File>){
    val fileIds = diskMap.map { it.fileId }
    
    fileIds.sortedDescending().forEach { 
        moveFileIfPossible(diskMap, it)
        combineEmptySpaces(diskMap)
    }
}

fun moveFileIfPossible(diskMap: MutableList<Part2File>, fileId: Int) {
    val file = diskMap.find { it.fileId == fileId }!!
    val fileIndex = diskMap.indexOfFirst { it.fileId == fileId }
    
    diskMap.forEachIndexed { index, it ->
        if (it.fileId == -1 && it.size >= file.size && it.endIndex <= file.startIndex) {
            diskMap[index] = Part2File(-1, it.startIndex + file.size, it.endIndex, it.size - file.size) //Update empty space
            diskMap[fileIndex] = Part2File(-1, file.startIndex, file.endIndex, file.size) //Remove file from original position
            diskMap.add(index, Part2File(file.fileId, it.startIndex, it.startIndex + file.size - 1, file.size)) //Move file
            
            return
        }
    }
}

fun combineEmptySpaces(diskMap: MutableList<Part2File>) {
    val repeatingIndices = mutableListOf<Int>()
    var combinedEmptySpaces: Part2File = Part2File(-1, 0, 0, 0)
    
    run loop@{
        diskMap.forEachIndexed { index, it ->
            if (it.fileId == -1) {
                repeatingIndices.add(index)
            }

            if (it.fileId != -1) {
                if (repeatingIndices.size > 1) {
                    combinedEmptySpaces = Part2File(-1, diskMap[repeatingIndices[0]].startIndex, 0, 0)
                    repeatingIndices.forEach { emptySpaceIndex ->
                        combinedEmptySpaces = Part2File(-1, combinedEmptySpaces.startIndex, 0, combinedEmptySpaces.size + diskMap[emptySpaceIndex].size)
                    }

                    combinedEmptySpaces =
                        Part2File(-1, combinedEmptySpaces.startIndex, combinedEmptySpaces.startIndex + combinedEmptySpaces.size - 1, combinedEmptySpaces.size)
                    return@loop
                } else {
                    repeatingIndices.clear()
                }
            }
        }
    }
    
    if (repeatingIndices.size < 2) {
        return
    }
    
    diskMap[repeatingIndices[0]] = combinedEmptySpaces
    repeatingIndices.forEachIndexed { index, it ->
        if (index != 0) {
            diskMap.removeAt(it - index + 1)
        }
    }
}

fun convertToBlockList(diskMap: List<Part2File>): List<File> {
    val blockList = mutableListOf<File>()
    
    diskMap.forEach{
        for (i in 0 until it.size) {
            blockList.add(File(it.fileId))
        }
    }
    
    return blockList
}

data class File (val fileId: Int)

data class Part2File (val fileId : Int, val startIndex : Int, val endIndex : Int, val size: Int)
