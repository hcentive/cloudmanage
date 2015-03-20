//######
//## This work is licensed under the Creative Commons Attribution-Share Alike 3.0 
//## United States License. To view a copy of this license, 
//## visit http://creativecommons.org/licenses/by-sa/3.0/us/ or send a letter 
//## to Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
//######

(function($){
 $.fn.idleTimeout = function(options) {
    var defaults = {
			inactivity: 1200000, //20 Minutes
			noconfirm: 10000, //10 Seconds
			sessionAlive: 30000, //10 Minutes
			redirect_url: '/js_sandbox/',
			click_reset: true,
			alive_url: '/js_sandbox/',
			logout_url: '/js_sandbox/'
		}
    
    //##############################
    //## Private Variables
    //##############################
    var opts = $.extend(defaults, options);
    var liveTimeout, confTimeout, sessionTimeout;
    var modal = "<div id='modal_pop'><p>You are about to be signed out due to inactivity.</p></div>";
    //##############################
    //## Private Functions
    //##############################
    var start_liveTimeout = function()
    {
      clearTimeout(liveTimeout);
      clearTimeout(confTimeout);
      if(opts.sessionAlive)
      {
        clearTimeout(sessionTimeout);
        sessionTimeout = setTimeout(keep_session, opts.sessionAlive);
      }
      else{
    	  liveTimeout = setTimeout(logout, opts.inactivity);
      }
    }
    
    var logout = function()
    {
      
      confTimeout = setTimeout(redirect, opts.noconfirm);
      jAlert('Your session is about to expire, please click OK to keep your session active.', 'WARNING', function(r)
	  {
    	  stay_logged_in();
	  });

    }
    
    var redirect = function()
    {
      if(opts.logout_url)
      {
        $.get(opts.logout_url);
      }
      window.location.href = opts.redirect_url;
    }
    
    var stay_logged_in = function(el)
    {
      start_liveTimeout();
      if(opts.alive_url)
      {
        (new Image()).src = opts.alive_url+"?"+new Date().getTime();
      }
    }
    
    var keep_session = function()
    {
      (new Image()).src = opts.alive_url+"?"+new Date().getTime();
      clearTimeout(sessionTimeout);
      sessionTimeout = setTimeout(keep_session, opts.sessionAlive);
    } 
    
    //###############################
    //Build & Return the instance of the item as a plugin
    // This is basically your construct.
    //###############################
    return this.each(function() {
      obj = $(this);
      start_liveTimeout();
      if(opts.click_reset)
      {
        $(document).bind('click', start_liveTimeout);
      }
      /*if(opts.sessionAlive)
      {
        keep_session();
      }*/
    });
    
 };
})(jQuery);
