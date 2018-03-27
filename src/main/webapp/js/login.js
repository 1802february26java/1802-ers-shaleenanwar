window.onload = () => {
    //Redirect user to the right URL if he comes from somewhere else
    if(window.location.pathname !== '/ERS/login.do'){
        window.location.replace('login.do');
    }
        
    //Login Event Listener
    
        document.getElementById("login").addEventListener("click", () => {
            let username = document.getElementById("username").value;
            let password = document.getElementById("password").value;
    
            //AJAX Logic
            //xhr variable can be called anything, you create your xhr object
            let xhr = new XMLHttpRequest();
    
            //before running this, have to check onreadystate change and if statement
            //Add logic
            xhr.onreadystatechange = () => {
                //If the request is DONE (4), and everything is OK
                if(xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200){
                    //Getting JSON from response body
                    let data = JSON.parse(xhr.responseText);
                    console.log(data);
    
                    //Call login response processing
                    login(data);
                }
            };
            
            //Doing a HTTP to a specific endpoiint
            xhr.open("POST",`login.do?username=${username}&password=${password}`);
    
            //Sending our request
            xhr.send();
    
        })
    
    }
    
    function login(data){
        //If message is a member of the JSON, it was AUTHENTICATION FAILED
        if(data.message){
            document.getElementById("loginMessage").innerHTML = '<span class="label label-danger label-center">Wrong credentials.</span>';
        }
        else {
            //Using sessionStorage of JavaScript
            //Storing key value pairs by fields
            sessionStorage.setItem("employeeId", data.id);
            sessionStorage.setItem("employeeUsername", data.username);
            sessionStorage.setItem("employeefirstName", data.firstName);
            window.location.replace("home.do");
    
        }
    }