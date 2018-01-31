

$('.close').on('click', function() {
  $('.input-container').remove();
});


$("#signupform").submit(function(event) {
  
    var passwd = $('input[name=passwd]').val();
    var confpasswd = $('input[name=confpasswd]').val();
    if(passwd != confpasswd) {
        alert("passwords do not match");
        return;
    } 
    var phone = $('input[name=phone]').val();
    if(!validatePhone(phone)){
    	alert("invalid phone format");
    	return;
    }
    
    var x = grecaptcha.getResponse();
    if(x.length < 1) {
        alert("please check you are not a robot");
        return;
    } 
    
    var formData = {
    	  'regcode'   : $('input[name=regcode]').val(),
          'fname'     : $('input[name=fname]').val(),
          'lname'     : $('input[name=lname]').val(),
          'phone'     : $('input[name=phone]').val(),
          'hnumber'   : $('input[name=hnumber]').val(),
          'street'    : $('input[name=street]').val(),
          'city'      : $('input[name=city]').val(),
          'state'     : $('input[name=state]').val(),
          'zip'       : $('input[name=zip]').val(),
          'country'   : $('input[name=country]').val(),
          'passwd'    : $('input[name=passwd]').val(),
          'gresp'	  : x
    };
	    
	event.preventDefault();

	var data = JSON.stringify(formData)
	var url = "https://home-tripolay.rhcloud.com/homerest/usr/signup";
	ajaxcall(url,data,"POST",render_signup,render_singup_error);
	
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

    $div.dialog({
        modal: true,
        height: 450,
        width: 500,
        show: {
        effect: "blind",
        duration: 1000
    },
    hide: {
        effect: "explode",
        duration: 1000
    },
    close: function(event, ui){
    	$(".close").trigger( "click" );
        $(this).dialog('destroy').remove();
        
        // redirect to home page.
        // window.location.replace("https://home-tirpolay.. ..........................");
        
    }
    }).prev(".ui-dialog-titlebar").css("background","#ffa41a");
}


function validatePhone(phone) {
	var re = /^\(?([0-9]{3})\)?[-. ]?([0-9]{3})[-. ]?([0-9]{4})$/;
	// test is better than match
	//if(phone.match(pattern)) 
	//	return true;
	//else
	//	return false;
	return re.test(phone); 
}


