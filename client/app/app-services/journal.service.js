(function () {
    'use strict';

    angular
        .module('app')
        .factory('JournalService', JournalService);

    JournalService.$inject = ['$http', '$q'];
    function JournalService($http, $q) {
        var service = {};

        service.GetAll = GetAll;
        service.Create = Create;
        service.Delete = Delete;

        return service;

        function GetAll() {
            var journals = [{id:'1', filename:'file1', tags:'tag1, tag2'}, 
                    {id:'2', filename:'file2', tags:'tag3, tag4'}];
            
            var deferred = $q.defer();
            deferred.resolve(journals);
            return deferred.promise;
        
            //return $http.get('/api/users').then(handleSuccess, handleError('Error getting all users'));
        }

        function Create(file, tags) {
            var deferred = $q.defer();
            deferred.resolve({ success: true });
            return deferred.promise;
        
            //return $http.post('/api/users', user).then(handleSuccess, handleError('Error creating journal'));
        }
        
        function Delete(id) {
            return $http.delete('/api/users/' + id).then(handleSuccess, handleError('Error deleting journal'));
        }

        // private functions

        function handleSuccess(res) {
            return res.data;
        }

        function handleError(error) {
            return function () {
                return { success: false, message: error };
            };
        }
    }

})();
