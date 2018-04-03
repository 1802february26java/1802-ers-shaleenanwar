window.onload = () =>{
    document.getElementById("loggedEmployee").innerHTML = sessionStorage.getItem("username");
    document.getElementById("firstName").innerHTML = " " + sessionStorage.getItem("firstName");
    document.getElementById("lastName").innerHTML = " " + sessionStorage.getItem("lastName");
    document.getElementById("userName").innerHTML = " " + sessionStorage.getItem("username");
    document.getElementById("email").innerHTML = " " + sessionStorage.getItem("email");

document.getElementById("submit").addEventListener("click", () => {
    let firstname = document.getElementById("firstNameText").value;
    let lastname = document.getElementById("lastNameText").value;
    let username = document.getElementById("usernameText").value;
    let email = document.getElementById("emailText").value;

    // Ajax
    let xhr =  new XMLHttpRequest();

    xhr.onreadystatechange = () => {
        if(xhr.onreadystatechange === XMLHttpRequest.DONE && xhr.status === 200) {
            let data = JSON.parse(xhr.responseText);
            console.log(data);
            submit(data);
        }
    };

    xhr.open("POST", `account.do?firstname=${firstname}&lastname=${lastname}&username=${username}&email=${email}`)
    xhr.send();

    document.getElementById("loginMessage").innerHTML = '<span class="label label-success label-center">Profile Change Successful</span>';
    sessionStorage.setItem("employeeFirstName", document.getElementById("firstNameText").value);
    sessionStorage.setItem("employeeLastName", document.getElementById("lastNameText").value);
    sessionStorage.setItem("employeeUsername", document.getElementById("usernameText").value);
    sessionStorage.setItem("email",document.getElementById("emailText").value);
    setTimeout(() =>{ window.location.replace("account.do");}, 2000);

})
}

function submit(data) {
    if(data.message === "REGISTRATION SUCCESSFUL") {
        document.getElementById("loginMessage").innerHTML = '<span class="label label-danger label-center">Profile Change Successfule</span>';
        setTimeout(() =>{ window.location.replace("profile.do");}, 2000);
    }
    //Something went wrong
    else {
        document.getElementById("loginMessage").innerHTML = '<span class="label label-success label-center">Something went wrong.</span>';
    }
}