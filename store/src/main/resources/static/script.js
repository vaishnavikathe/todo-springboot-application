// ================= TOKEN MANAGEMENT =================
// JWT replaces CSRF logic. We retrieve the token stored during login.
const getToken = () => localStorage.getItem("jwtToken");

// ================= GLOBAL STATE =================
let currentPage = 0;
let pageSize = 5;
let totalPages = 0;
let currentSearch = "";

// ================= LOAD TODOS (PAGINATED) =================
async function loadTodos() {
    let url = `/api/todos?page=${currentPage}&size=${pageSize}`;

    if (currentSearch !== "") {
        url = `/api/todos/search?title=${encodeURIComponent(currentSearch)}&page=${currentPage}&size=${pageSize}`;
    }

    // Every GET request must now include the Bearer token in headers
    const response = await fetch(url, {
        headers: {
            "Authorization": "Bearer " + getToken()
        }
    });

    if (response.status === 401 || response.status === 403) {
        // If token is invalid or expired, redirect to login
        window.location.href = "/login";
        return;
    }

    const data = await response.json();
    totalPages = data.totalPages;
    renderTodos(data.content);
    updatePaginationInfo();
}

//================= RENDER TODOS =================
function renderTodos(todos) {
    const tableBody = document.getElementById("todoTableBody");
    tableBody.innerHTML = "";

    todos.forEach(todo => {
        const row = document.createElement("tr");

        row.innerHTML = `
            <td>${todo.title}</td>
            <td>${todo.description || ""}</td>
            <td>${todo.status}</td>
            <td>
                <button onclick="editTodo(${todo.id}, '${todo.title}', '${todo.description || ""}')">Edit</button>
                <button onclick="deleteTodo(${todo.id})">Delete</button>
                ${todo.status === 'PENDING'
                    ? `<button onclick="markCompleted(${todo.id})">Complete</button>`
                    : ''}
            </td>
        `;

        tableBody.appendChild(row);
    });
}

//================= EDIT =================
function editTodo(id, title, description) {
    document.getElementById("todoId").value = id;
    document.getElementById("input").value = title;
    document.getElementById("description").value = description;
}

//================= SAVE (ADD / UPDATE) =================
async function saveTodo() {
    const id = document.getElementById("todoId").value;
    const title = document.getElementById("input").value.trim();
    const description = document.getElementById("description").value.trim();

    if (!title) {
        alert("Title required");
        return;
    }

    const url = id ? `/api/todos/${id}` : "/api/todos";
    const method = id ? "PUT" : "POST";

    await fetch(url, {
        method,
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + getToken() // Include JWT Token
        },
        body: JSON.stringify({ title, description })
    });

    document.getElementById("todoId").value = "";
    document.getElementById("input").value = "";
    document.getElementById("description").value = "";

    loadTodos();
}

//================= DELETE =================
async function deleteTodo(id) {
    if (!confirm("Are you sure you want to delete this ToDo?")) return;

    await fetch(`/api/todos/${id}`, {
        method: "DELETE",
        headers: {
            "Authorization": "Bearer " + getToken() // Include JWT Token
        }
    });

    loadTodos();
}

// ================= MARK COMPLETED =================
async function markCompleted(id) {
    await fetch(`/api/todos/${id}/complete`, {
        method: "PUT",
        headers: {
            "Authorization": "Bearer " + getToken() // Include JWT Token
        }
    });

    loadTodos();
}

// ================= LOGOUT =================
function logout() {
    localStorage.removeItem("jwtToken"); // Clear the token
    window.location.href = "/login"; // Redirect to login page
}

// ================= SEARCH =================
function searchTodos() {
    currentSearch = document.getElementById("searchInput").value.trim();
    currentPage = 0;
    loadTodos();
}

// ================= PAGINATION UI =================
function updatePaginationInfo() {
    document.getElementById("pageInfo").innerText =
        `Page ${currentPage + 1} of ${totalPages}`;

    document.getElementById("prevBtn").disabled = currentPage === 0;
    document.getElementById("nextBtn").disabled = currentPage >= totalPages - 1;
}

// ================= EVENT LISTENERS =================
document.addEventListener("DOMContentLoaded", () => {
    // Check if token exists, otherwise redirect to login immediately
    if (!getToken()) {
        window.location.href = "/login";
        return;
    }

    document.getElementById("searchInput")
        .addEventListener("keyup", searchTodos);

    document.getElementById("prevBtn")
        .addEventListener("click", () => {
            if (currentPage > 0) {
                currentPage--;
                loadTodos();
            }
        });

    document.getElementById("nextBtn")
        .addEventListener("click", () => {
            if (currentPage < totalPages - 1) {
                currentPage++;
                loadTodos();
            }
        });

    loadTodos();
});