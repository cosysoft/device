/**
 * Created by 兰天 on 2015/3/21.
 */
var app = angular.module('KeeperApp', ['ngMaterial', 'ngRoute','deviceControllers']);

app.config(['$routeProvider',
    function ($routeProvider) {
        $routeProvider.
            when('/device', {
                templateUrl: 'device.html',
                controller: 'DeviceCtrl'
            }).
            otherwise({
                redirectTo: '/device'
            });
    }]);
app.controller('AppCtrl', ['$scope', '$mdSidenav', function ($scope, $mdSidenav) {
    $scope.toggleSidenav = function (menuId) {
        $mdSidenav(menuId).toggle();
    };

    $scope.menus = [{name: 'Device List', hash: '/device'}];
}]);

