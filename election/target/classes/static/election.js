document.getElementById("createAccountButton").addEventListener("click", function() {
    let username = document.getElementById("setUsernameInput").value;
    let password = document.getElementById("setPasswordInput").value;
    let confirmPassword = document.getElementById("confirmPasswordInput").value;
    let phoneNumber = document.getElementById("setPhoneInput").value;
    let emailAdress = document.getElementById("setEmailInput").value;
    let firstName = document.getElementById("setFirstNameInput").value;
    let lastName = document.getElementById("setLastNameInput").value;
    let age = document.getElementById("setAgeInput").value;
    let profession = document.getElementById("setProfessionInput").value;

    if (password !== confirmPassword) {
        alert("Passwords do not match!");
        return;
    }

    let person = {
        username: username,
        password: password,
        phoneNumber: phoneNumber,
        emailAdress: emailAdress,
        firstName: firstName,
        lastName: lastName,
        age: age,
        profession: profession,
        status: "voter",
        bibliography: "",
        motto: "",
        nrOfVotes: 0
    };

    fetch("/persons", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(person)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Network response was not ok");
            }
            return response.json();
        })
        .then(data => {
            console.log("Account created successfully:", data);
            sessionStorage.setItem("username", username);
            window.location.href = "homePage";
        })
        .catch(error => {
            console.error("Error:", error);
            alert("An error occurred while creating the account!");
        });
});

document.getElementById("logInButton").addEventListener("click", function() {
    let username = document.getElementById("logInUsernameInput").value;
    let password = document.getElementById("logInPasswordInput").value;

    let credentials = {
        username: username,
        password: password
    };

    fetch("/persons/log-in", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(credentials)
    })
        .then(response => {
            if (response.ok) {
                sessionStorage.setItem("username", username);
                window.location.href = "homePage";
            } else {
                alert("Invalid username or password");
            }
        })
        .catch(error => {
            console.error("Error:", error);
            alert("An error occurred while logging in!");
        });
});
