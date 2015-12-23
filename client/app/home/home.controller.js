(function () {
    'use strict';

    angular
        .module('app')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'UserService', 'JournalService', 'FlashService', '$rootScope'];
    function HomeController($scope, UserService, JournalService, FlashService, $rootScope) {
        var vm = this;

        $scope.file = null;
        vm.fileReady = false;
        vm.user = null;
        vm.allUsers = [];
        vm.deleteUser = deleteUser;
        vm.journals = [];
        vm.deleteJournal = deleteJournal;
        vm.upload = upload;

        initController();

        function initController() {
            loadCurrentUser();
            loadAllUsers();
            loadJournals();
        }

        function loadCurrentUser() {
            UserService.GetByUsername($rootScope.globals.currentUser.username)
                .then(function (user) {
                    vm.user = user;
                });
        }

        function loadAllUsers() {
            UserService.GetAll()
                .then(function (users) {
                    vm.allUsers = users;
                });
        }

        function deleteUser(id) {
            UserService.Delete(id)
            .then(function () {
                loadAllUsers();
            });
        }

        function loadJournals() {
            JournalService.GetAll()
                .then(function (journals) {
                    vm.journals = journals;
                });
        }

        function deleteJournal(id) {
            JournalService.Delete(id)
            .then(function () {
                loadJournals();
            });
        }
        
        $scope.uploadedFile = function(element) {
            $scope.$apply(function($scope) {
                $scope.file = element.files[0];
                vm.fileReady = true;           
            });
        }

        function upload() {
            vm.dataLoading = true;
            JournalService.Create($scope.file, vm.uploadedJournal.tags)
                .then(function (response) {
                    if (response.success) {
                        FlashService.Success('Upload successful', true);
                        vm.fileReady = false;
                    } else {
                        FlashService.Error(response.message);
                    }
                    vm.dataLoading = false;
                });
        }
    }

})();