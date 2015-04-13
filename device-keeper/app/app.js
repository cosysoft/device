/**
 * Created by 兰天 on 2015/3/21.
 */
var app = angular.module('KeeperApp', ['ngMaterial', 'ngRoute', 'deviceControllers']);

app.config(['$routeProvider',
    function ($routeProvider) {
        $routeProvider.
            when('/device', {
                templateUrl: 'device/device-list.html',
                controller: 'DeviceCtrl'
            }).
            when('/node', {
                templateUrl: 'device/node-list.html'
            }).
            otherwise({
                redirectTo: 'device'
            });
    }]);

app.controller('AppCtrl', ['$scope', '$mdSidenav', function ($scope, $mdSidenav) {
    $scope.toggleSidenav = function (menuId) {
        $mdSidenav(menuId).toggle();
    };

    $scope.menus = [{name: 'Device List', hash: '/device'}, {
        name: 'Node List', hash: '/node'
    }];


    $scope.isSectionSelected = function (section) {
        var selected = false;
        var openedSection = $scope.menus.openedSection;
        if (openedSection === section) {
            selected = true;
        }
        return selected;
    }
}]);

