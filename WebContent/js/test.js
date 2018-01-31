


$('#btn').click(function () {
    
	
	var url = "http://localhost:8080/tripolay/Test";
	ajaxcall(url,data,"GET",render_signup,render_singup_error);
	msg = "<h3>Could not register. </h3>" ;
	title = "Result:";
	//outputmsg(title,msg);
	alert(msg);
	$("#target").append(msg);
	
});



function render_signup(data)
{
	msg = "<h3>You are now registered.</h3>";
	title = "Registeration";
	outputmsg(title,msg);
}
function render_singup_error(data)
{
	reason = data.msg;
	msg = "<h3>Could not register." + reason + " </h3>" ;
	title = "Registeration";
	outputmsg(title,msg);
}

//for POST JSON, must have header since client will not supply it automatically
function ajaxcall(url,data,type,handle_S,handle_F)
{
	 $.ajax({
	        type: type,
	        url: url,
	        data:  data,
	        dataType: "json",
	        headers: {
	            'Content-Type':'application/json'
	        	},
	        
	        //if received a response from the server
	        success: function( data, textStatus, jqXHR) {
	            
	             if(data.success)
	             {
	            	handle_S(data);
	             } 
	             else
	             {
	                 //$("#ajaxResponse").html("<div><b>data in Invalid!</b></div>");
	            	 handle_F(data);
	             }
	        },
	        //If there was no response from the server
	        error: function(jqXHR, textStatus, errorThrown){
	             console.log("Something really bad happened " + textStatus);
	             $('.spinner2').css({'display': 'none'});
	            /// alert(textStatus);
	             
	        },
	        //capture the request before it was sent to server
	        beforeSend: function(jqXHR, settings){
	            
	        },
	        //this is called after the response or error functions
	        complete: function(jqXHR, textStatus){
	            
	        }
	    });   
}

function outputmsg(title,msg)
{
    $div = $('<div id="msg" title="' + title + '">').css("display","inline");

    $div.append(msg);

  
}


