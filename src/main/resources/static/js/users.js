document.getElementById('loginForm').addEventListener('submit', function (e) {
    e.preventDefault();

    const username = document.getElementById('loginUsername').value;
    const password = document.getElementById('loginPassword').value;

    fetch('/login', {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: `username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}`
    }).then(res => res.json())
        .then(data => {
            if (data.status === "success") {
                document.getElementById('loginContainer').style.display = 'none';
                document.getElementById('adminContainer').style.display = 'block';

                if (data.roles.includes("ROLE_ADMIN")) {
                    loadUsers();
                    loadRoles();
                } else {
                    loadUserInfo(); // отдельная функция для обычного пользователя
                }
            } else {
                alert('Ошибка логина');
            }
        });
});


function loadUsers() {
    fetch('/api/admin/users')
        .then(res => res.json())
        .then(users => {
            const tbody = document.querySelector('#usersTable tbody');
            tbody.innerHTML = '';

            users.forEach(user => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.yearOfBirth}</td>
                    <td>${user.roles.join(', ')}</td>
                    <td>
                        <button onclick="editUser(${user.id})">Edit</button>
                        <button onclick="deleteUser(${user.id})">Delete</button>
                    </td>
                `;
                tbody.appendChild(tr);
            });
        });
}


function loadRoles() {
    fetch('/api/admin/roles')
        .then(res => res.json())
        .then(roles => {
            const createContainer = document.getElementById('rolesContainer');
            const editContainer = document.getElementById('editRolesContainer');
            createContainer.innerHTML = '';
            editContainer.innerHTML = '';

            roles.forEach(role => {
                // CREATE
                createContainer.innerHTML += `
                    <label>
                        <input type="checkbox" name="roles" value="${role.id}"> ${role.name}
                    </label><br>
                `;
                // EDIT
                editContainer.innerHTML += `
                    <label>
                        <input type="checkbox" value="${role.id}" data-name="${role.name}"> ${role.name}
                    </label><br>
                `;
            });
        });
}


document.getElementById('createForm').addEventListener('submit', function (e) {
    e.preventDefault();

    const username = document.getElementById('createUsername').value;
    const yearOfBirth = document.getElementById('createYearOfBirth').value;
    const password = document.getElementById('createPassword').value;
    const roleIds = Array.from(document.querySelectorAll('#rolesContainer input:checked'))
        .map(el => parseInt(el.value));

    fetch('/api/admin/users', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({username, yearOfBirth, password, roleIds})
    }).then(res => {
        if (res.ok) {
            loadUsers();
            document.getElementById('createForm').reset();
        } else alert('Error creating user');
    });
});


function deleteUser(id) {
    fetch(`/api/admin/users/${id}`, {method: 'DELETE'})
        .then(res => {
            if (res.ok) loadUsers(); else alert('Error deleting user');
        });
}


function editUser(id) {
    fetch(`/api/admin/users/${id}`)
        .then(res => res.json())
        .then(user => {
            document.getElementById('editId').value = user.id;
            document.getElementById('editUsername').value = user.username;
            document.getElementById('editYearOfBirth').value = user.yearOfBirth;


            const checkboxes = document.querySelectorAll('#editRolesContainer input');
            checkboxes.forEach(cb => {
                cb.checked = user.roles.includes(cb.dataset.name);
            });
        });
}


document.getElementById('editForm').addEventListener('submit', function (e) {
    e.preventDefault();

    const id = document.getElementById('editId').value;
    const username = document.getElementById('editUsername').value;
    const yearOfBirth = document.getElementById('editYearOfBirth').value;
    const password = document.getElementById('editPassword').value;
    const roleIds = Array.from(document.querySelectorAll('#editRolesContainer input:checked'))
        .map(el => parseInt(el.value));

    fetch(`/api/admin/users/${id}`, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({username, yearOfBirth, password, roleIds})
    }).then(res => {
        if (res.ok) {
            loadUsers();
            document.getElementById('editForm').reset();
        } else alert('Error updating user');
    });
});