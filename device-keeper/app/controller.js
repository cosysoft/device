/**
 * Created by 兰天 on 2015/3/31.
 */
var deviceControllers = angular.module('deviceControllers', []);

deviceControllers.controller('DeviceCtrl', ['$scope', '$http',
    function ($scope, $http) {
        $http.get('/api/devices').success(function(data) {
            $scope.devices = data;
        });
    }]);
