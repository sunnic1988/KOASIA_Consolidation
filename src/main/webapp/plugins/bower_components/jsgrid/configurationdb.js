(function() {

    var configurationdb = {

        loadData: function(filter) {		
        	
       	 return $.ajax({
             type: "POST",
             url: "../SelectConfigurationServlet",
             dataType: "json", //必写
             contentType: "application/json", //必写
             data: JSON.stringify(filter)
         });
    	

                return $.grep(this.clients, function(client) {
                return (!filter.$("Cons Area") || client.$("Cons Area").indexOf(filter.$("Cons Area")) > -1)
                    && (!filter.Age || client.Age === filter.Age)
                    && (!filter.Address || client.Address.indexOf(filter.Address) > -1)
                    && (!filter.Country || client.Country === filter.Country)
                    && (filter.Married === undefined || client.Married === filter.Married);
            });
        },

        insertItem: function(insertingClient) {
            this.clients.push(insertingClient);
        },

        updateItem: function(updatingClient) { },

        deleteItem: function(deletingClient) {
            var clientIndex = $.inArray(deletingClient, this.clients);
            this.clients.splice(clientIndex, 1);
        }

    };

    window.configurationdb = configurationdb;


    configurationdb.clients = [] ;

}());


 