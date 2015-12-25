(function () {
    'use strict';

    angular
        .module('app')
        .factory('UserService', UserService);

    UserService.$inject = ['$http'];
    function UserService($http) {
        var service = {};

        service.GetAll = GetAll;
        service.GetById = GetById;
        service.GetByUsername = GetByUsername;
        service.Create = Create;
        service.Update = Update;
        service.Delete = Delete;

        return service;

        function Create(user) {
            //TODO use global const for address and move to HTTPS
            return $http.post('http://localhost:8080/services/publisherAccount/register', user).then(handleRegistrationSuccess, handleError('Error creating user'));
        }

        // private functions

        function handleRegistrationSuccess(res) {
            return { success: true };
        }

        function handleError(error) {
            return function () {
                return { success: false, message: error };
            };
        }
    }

})();
