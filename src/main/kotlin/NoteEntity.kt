import java.util.*

open class NoteEntity {
    private val scanner = Scanner(System.`in`)

    private var archives = mutableListOf<Archive>()
    private val archiveMenu = mutableSetOf<Pair<Int, String>>()
    private val noteMenu = mutableSetOf<Pair<Int, String>>()

    fun startArchive() {
        while (true) {
            println("Для выбора действия введите нужную цифру:")
            showArchiveMenu()

            val archiveMenuNumber = scanner.nextLine().toIntOrNull()
            if (archiveMenuNumber in 1..archiveMenu.size) {
                when (archiveMenuNumber) {
                    1 -> createArchive()
                    archiveMenu.size -> return
                    else -> showArchive(archiveMenu.first{it.first == archiveMenuNumber}.second)
                }
            } else {
                println("Введите цифру, соответствующую пунктам меню")
            }
        }
    }

    private fun showArchiveMenu() {
        archiveMenu.add(1 to "Создать архив")
        archiveMenu.add(2 to "Выход")
        if (archives.isNotEmpty()) {
            archiveMenu.removeAll(archiveMenu.filter{it.second == "Выход"}.toSet())
            for ((i, archive) in archives.withIndex()) {
                archiveMenu.add(i+2 to archive.name)
            }
            archiveMenu.add(archiveMenu.size+1 to "Выход")
        }

        for (i in archiveMenu) {
            println("${i.first}. ${i.second}")
        }
    }

    private fun showNoteMenu(archiveName: String) {
        if (noteMenu.size > 2) {
            noteMenu.clear()
        }
        noteMenu.add(1 to "Создать заметку")
        noteMenu.add(2 to "Назад")

        val archiveNotes = archives.first { it.name == archiveName }.notesInArchive
        if (archiveNotes.isNotEmpty()) {
            noteMenu.removeAll(noteMenu.filter{it.second == "Назад"}.toSet())
            for ((i, note) in archiveNotes.withIndex()) {
                noteMenu.add(i+2 to note.noteName)
            }
            noteMenu.add(noteMenu.size+1 to "Назад")
        }

        for (i in noteMenu) {
            println("${i.first}. ${i.second}")
        }

    }

    private fun createArchive() {
        println("Введите название архива")
        val archiveName = scanner.nextLine()
        if (archives.any { it.name == archiveName }) {
            println("Архив с таким названием уже существует, введите другое название")
        } else if (archiveName.isBlank()) {
            println("Название архива не может быть пустым")
        } else {
            archives.add(Archive(archiveName))
            println("Создан архив с названием \"$archiveName\"")
            startNote(archiveName)
        }
    }

    private fun showArchive(archiveName: String) {
        println("Архив \"$archiveName\"")
        println("Для выбора действия введите нужную цифру:")
        while (true) {
            showNoteMenu(archiveName)
            val noteMenuNumber = scanner.nextLine().toIntOrNull()
            if (noteMenuNumber in 1..noteMenu.size) {
                when (noteMenuNumber) {
                    1 -> createNote(archiveName)
                    noteMenu.size -> return
                    else -> showNote(archiveName, noteMenu.first { it.first == noteMenuNumber }.second)
                }
            } else {
                println("Введите цифру, соответствующую пунктам меню")
            }
        }
    }

    private fun startNote(archiveName: String) {
        while (true) {
            println("Для выбора действия введите нужную цифру:")
            showNoteMenu(archiveName)
            val noteMenuNumber = scanner.nextLine().toIntOrNull()
            if (noteMenuNumber in 1..noteMenu.size) {
                when (noteMenuNumber) {
                    1 -> createNote(archiveName)
                    noteMenu.size -> return
                    else -> showNote(archiveName, noteMenu.first { it.first == noteMenuNumber }.second)
                }
            } else {
                println("Введите цифру, соответствующую пунктам меню")
            }
        }
    }

    private fun createNote(archiveName: String) {
        println("Введите название заметки")
        val noteName = scanner.nextLine()
        val notes = archives.first { it.name == archiveName }.notesInArchive
        if (notes.any { it.noteName == noteName}) {
            println("Заметка с таким названием уже существует, введите другое название")
        } else if (noteName.isBlank()) {
            println("Название заметки не может быть пустым")
        } else {
            while(true) {
                println("Введите содержимое заметки")
                val noteBody = scanner.nextLine()
                if (noteBody.isBlank()) {
                    println("Содержимое заметки не может быть пустым")
                } else {
                    notes.add(Note(noteName, noteBody))
                    println("Создана заметка с названием \"$noteName\"")
                    return
                }
            }
        }
    }

    private fun showNote(archiveName: String, noteName: String) {
        val noteToShow = archives.first {it.name == archiveName}.notesInArchive.find { it.noteName == noteName }
        println("Заметка \"${noteToShow!!.noteName}\"\n${noteToShow.noteBody}")
    }
}