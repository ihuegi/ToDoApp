# ToDo-App
To-Do app with two sections, Active and Completed, using Jetpack Compose. The app demonstrates the concepts we covered in class: data classes, state, remember/rememberSaveable, and state hoisting. The UI is responsive, accessible, and can handle common edge cases gracefully.

## Features

* Add item
* List items (active)
* List items (Completed)
* Sections & empty states
* Delete tasks from either list.

---
<img width="394" height="883" alt="Screenshot 2025-10-03 075947" src="https://github.com/user-attachments/assets/77ef5ce9-81c6-4b37-b780-eb0b954865d8" />
---

## Concepts Demonstrated

### 1. **Data Class**

* The `Task` data class models each task with three properties:

  * `id`: unique identifier for each task
  * `title`: task description
  * `isCompleted`: Boolean state indicating if the task is completed

```kotlin
data class Task(
    val id: Int,
    val title: String,
    val isCompleted: Boolean = false
)
```

### 2. **State**

* `rememberSaveable` is used to store and restore state across recompositions and configuration changes (like screen rotation).
* Examples:

  * `taskText` → holds text input from the user
  * `tasks` → list of all tasks
  * `validationMessage` → displays error messages

```kotlin
var taskText by rememberSaveable { mutableStateOf("") }
var tasks by rememberSaveable { mutableStateOf(listOf<Task>()) }
var validationMessage by remember { mutableStateOf("") }
```

### 3. **State Hoisting**

* State is **hoisted** so that child composables (`TaskList` and `TaskItem`) receive their state and callbacks from the parent (`ToDoApp`).
* This makes the UI modular and testable.
* Example: `onToggleTask` and `onDeleteTask` are passed down from `ToDoApp` into `TaskList` and then into `TaskItem`.

```kotlin
TaskList(
    tasks = activeTasks,
    onToggleTask = { toggleTask(it) },
    onDeleteTask = { deleteTask(it) }
)
```

### 4. **Composable Functions**

* The app is structured into smaller reusable UI pieces:

  * `ToDoApp()` → main app logic
  * `TaskList()` → displays a list of tasks
  * `TaskItem()` → UI for a single task

