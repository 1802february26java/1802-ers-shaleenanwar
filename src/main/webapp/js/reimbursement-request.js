window.onload = () => {
    /** **/
        
    //Register Event Listener
    
        document.getElementById("submit").addEventListener("click", () => {
                let amount = document.getElementById("amount").value;
                let description = document.getElementById("description").value;
                let reimbursementType = document.getElementById("reimbursementType").value;
                let reimbursementTypeId;
                if(reimbursementType==='COURSE'){
                    reimbursementTypeId = 2;
                }
                else if(reimbursementType==='CERTIFICATION'){
                    reimbursementTypeId = 3;
                }
                else if(reimbursementType==='TRAVELING'){
                    reimbursementTypeId = 4;
                }
                else{
                    reimbursementTypeId = 1;
                }
           
           
            //Get the rest of the fields
            let firstName = document.getElementById("firstName").value;
            let lastName = document.getElementById("lastName").value;
            let username = document.getElementById("username").value;
            let email = document.getElementById("email").value;

    
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
    
                    //Call registration response processing
                    register(data);
                }
            };
            
            //Doing a HTTP to a specific endpoint, sending first name and lastname as well as username and password
            xhr.open("POST",`register.do?firstName=${firstName}&lastName=${lastName}&username=${username}&password=${password}&email=${email}`);
    
            //Sending our request
            xhr.send();
    
        })
    
    }
    

    function disableAllComponents() {
        document.getElementById("firstName").setAttribute("disabled", "disabled");
        document.getElementById("lastName").setAttribute("disabled", "disabled");
        document.getElementById("username").setAttribute("disabled", "disabled");
        document.getElementById("password").setAttribute("disabled", "disabled");
        document.getElementById("repeatPassword").setAttribute("disabled", "disabled");
        document.getElementById("submit").setAttribute("disabled", "disabled");
    }

    function register(data){
        //If message is a member of the JSON, something went wrong
        if(data.message === "REGISTRATION SUCCESSFUL"){
            //Confirm registration and redirect to login
            disableAllComponents();
            document.getElementById("registrationMessage").innerHTML = '<span class="label label-success label-center">Registration successful.</span>';
           
            setTimeout(() => { window.location.replace("login.do"); }, 3000)
        }
        else {

            document.getElementById("registrationMessage").innerHTML = '<span class="label label-danger label-center">Something went wrong.</span>';
    
        }
    }