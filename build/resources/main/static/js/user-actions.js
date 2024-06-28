
function editUser(id) {
    const name = document.getElementById('edit-name-' + id).value;
    const email = document.getElementById('edit-email-' + id).value;

    fetch('/api/users/' + id, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({name: name, email: email})
    })
    .then(response => {
        if (response.ok) {
            showSuccessNotification();
        } else {
            console.error('Failed to edit user');
        }
    });
}

function deleteUser(id) {
    fetch('/api/users/' + id, {
        method: 'DELETE'
    })
    .then(response => {
        if (response.ok) {
            showSuccessNotification();
        } else {
            console.error('Failed to delete user');
        }
    });
}

function submitForm() {
    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;

    fetch('/api/users', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({name: name, email: email})
    })
    .then(response => {
        if (response.ok) {
            window.location.reload();
        } else {
            console.error('Failed to submit form');
        }
    });
}

function deleteSelectedUsers() {
    const selectedIds = Array.from(document.querySelectorAll('input[name="selectedUsers"]:checked'))
                           .map(checkbox => checkbox.value);

    if (selectedIds.length === 0) {
        alert('Please select at least one user to delete.');
        return;
    }

    fetch('/api/users', {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(selectedIds)
    })
    .then(response => {
        if (response.ok) {
            window.location.reload();
        } else {
            console.error('Failed to delete selected users');
        }
    });
}

function showSuccessNotification() {
    const notification = document.getElementById('successNotification');
    if (notification) {
        notification.style.display = 'block';
        setTimeout(() => {
            notification.style.display = 'none';
            window.location.reload();
        }, 3000);
    }
}
