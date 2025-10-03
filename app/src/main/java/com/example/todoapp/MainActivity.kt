package com.example.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    data class Task(
        val id: Int,
        val title: String,
        val isCompleted: Boolean = false
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ToDoApp()
                }
            }
        }
    }

    @Composable
    fun ToDoApp() {
        var taskText by rememberSaveable { mutableStateOf("") }
        var tasks by rememberSaveable { mutableStateOf(listOf<Task>()) }
        var validationMessage by remember { mutableStateOf("") }

        fun addTask(title: String) {
            val trimmedTitle = title.trim()
            if (trimmedTitle.isNotEmpty()) {
                val newTask = Task(id = (tasks.maxOfOrNull { it.id } ?: 0) + 1, title = trimmedTitle)
                tasks = tasks + newTask
                validationMessage = ""
            }
            else {
                validationMessage = "Task cannot be empty"
            }
        }

        fun toggleTask(task: Task) {
            tasks = tasks.map {
                if (it.id == task.id) it.copy(isCompleted = !it.isCompleted) else it
            }
        }

        fun deleteTask(task: Task) {
            tasks = tasks.filter { it.id != task.id }
        }

        val activeTasks = tasks.filter { !it.isCompleted }
        val completedTasks = tasks.filter { it.isCompleted }

        Column(
            modifier = Modifier
                .fillMaxSize().padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = taskText,
                    onValueChange = {
                        taskText = it
                        if (validationMessage.isNotEmpty()) validationMessage = ""
                    },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Enter a task") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    addTask(taskText)
                    if (validationMessage.isEmpty()) taskText = ""
                }) {
                    Text("Add")
                }
            }

            if (validationMessage.isNotEmpty()) {
                Text(
                    text = validationMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (activeTasks.isNotEmpty()) {
                Text("Items", style = MaterialTheme.typography.titleMedium)
                TaskList(
                    tasks = activeTasks,
                    onToggleTask = { toggleTask(it) },
                    onDeleteTask = { deleteTask(it) }
                )
            }
            else {
                Text("No items yet", modifier = Modifier.padding(8.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (completedTasks.isNotEmpty()) {
                Text("Completed Items", style = MaterialTheme.typography.titleMedium)
                TaskList(
                    tasks = completedTasks,
                    onToggleTask = { toggleTask(it) },
                    onDeleteTask = { deleteTask(it) }
                )
            }
            else {
                Text("No completed items yet", modifier = Modifier.padding(8.dp))
            }
        }
    }

    @Composable
    fun TaskList(
        tasks: List<Task>,
        onToggleTask: (Task) -> Unit,
        onDeleteTask: (Task) -> Unit
    ) {
        if (tasks.isEmpty()) {
            Text("No tasks here!", modifier = Modifier.padding(8.dp))
        }
        else {
            LazyColumn {
                items(tasks.size) { index ->
                    val task = tasks[index]
                    TaskItem(
                        task = task,
                        onToggleTask = onToggleTask,
                        onDelete = onDeleteTask
                    )
                }
            }
        }
    }

    @Composable
    fun TaskItem(
        task: Task,
        onToggleTask: (Task) -> Unit,
        onDelete: (Task) -> Unit
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onToggleTask(task) }
            )
            Text(
                text = task.title,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                style = MaterialTheme.typography.bodyLarge.copy(
                    textDecoration = if (task.isCompleted)
                        TextDecoration.LineThrough else TextDecoration.None
                )
            )
            IconButton(onClick = { onDelete(task) }) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete task")
            }
        }
    }
}
