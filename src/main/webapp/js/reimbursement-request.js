window.onload = () =>{

    document.getElementById("loggedEmployee").innerHTML = sessionStorage.getItem("username");

    document.getElementById("submitReimbursementButton").addEventListener("click", ()=>{
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

        let formdata = new FormData();
        formdata.append('amount',amount);
        formdata.append('description',description);
        formdata.append('reimbursementType',reimbursementType);
        formdata.append('reimbursementTypeId',reimbursementTypeId);

        //AJAX Logic

        let xhr = new XMLHttpRequest();

        xhr.onreadystatechange = () => {
            if(xhr.readyState === XMLHttpRequest.DONE && xhr.status ===200){
                console.log(xhr.responseText);
                let data = JSON.parse(xhr.responseText);
                console.log(data);
                createReimbursement(data);
            }
        };

    xhr.open("POST",`submit-reimbursement.do?amount=${amount}&description=${description}&reimbursementTypeId=${reimbursementTypeId}&reimbursementTypeName=${reimbursementType}`);
    xhr.send();
    })
}

function disableAllComponents(){
    document.getElementById("amount").setAttribute("disabled","disabled");
    document.getElementById("description").setAttribute("disabled","disabled");
}



function createReimbursement(data) {
    
     disableAllComponents();
  
      if(data.message === "SUCCESSFUL"){
        document.getElementById("submitReimbursementMessage").innerHTML = '<span class="label label-success label-center">CREATED SUCCESSFULLY.</span>';
        
      setTimeout(() =>{ window.location.replace("employee-home.do");}, 3000);
       
      }
      else{
        document.getElementById("submitReimbursementMessage").innerHTML = '<span class="label label-danger label-center">Something went wrong.</span>';
           
      }
}

