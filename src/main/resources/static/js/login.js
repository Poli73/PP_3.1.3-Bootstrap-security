const loginForm = document.getElementById('loginForm');
const usernameInput = document.getElementById("loginUsername");
const passwordInput = document.getElementById("loginPassword");

loginForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const response = await fetch('/login', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({
            username: usernameInput.value,
            password: passwordInput.value
        })
    });

    const data = await response.json();
    console.log(data);

    if (data.status === "success") {
        document.getElementById("loginContainer").style.display = "none";

        if (data.roles.includes("ROLE_ADMIN")) {
            document.getElementById("adminContainer").style.display = "block";
            loadUsers();
        } else if (data.roles.includes("ROLE_USER")) {
            document.getElementById("userContainer").style.display = "block";
            document.getElementById("userName").textContent = usernameInput.value;
            document.getElementById("userRoles").textContent = data.roles.join(", ");
            document.getElementById("yearOfBirth").textContent = "1990";
        }
    } else {
        alert("Login failed!");
    }
});

async function loadUsers() {
    const response = await fetch('/api/admin/users');
    const users = await response.json();

    let html = "<table border='1'><tr><th>ID</th><th>Username</th><th>Roles</th></tr>";
    users.forEach(u => {
        html += `<tr><td>${u.id}</td><td>${u.username}</td><td>${u.roles.join(", ")}</td></tr>`;
    });
    html += "</table>";

    document.getElementById('usersTable').innerHTML = html;
}