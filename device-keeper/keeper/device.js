/**
 * Created by 兰天 on 2015/3/30.
 */
var _ = require('underscore');

var Device = function () {
    this.devices = [
        {
            name: "xiaomi-2013022-ZDPB8PVCO7QGYDQG",
            udid: "ZDPB8PVCO7QGYDQG",
            avatarUri: "mock/avatar.png",
            osName: "ANDROID17(4.2.2)", nodeUri: 'http://172.0.0.1:8080/', nodeName: 'Windows8'
        },
        {
            name: "xiaomi-2013022-ZDPB8PVCO7QGYDQG",
            udid: "ZDPB8PVCO7QGYDQG",
            avatarUri: "mock/avatar.png",
            osName: "ANDROID17(4.2.2)", nodeUri: 'http://172.0.0.1:8080/', nodeName: 'Windows8'
        }, {
            name: "xiaomi-2013022-ZDPB8PVCO7QGYDQG",
            udid: "ZDPB8PVCO7QGYDQG",
            avatarUri: "mock/avatar.png",
            osName: "ANDROID17(4.2.2)", nodeUri: 'http://172.0.0.1:8080/', nodeName: 'Windows8'
        },
        {
            name: "xiaomi-2013022-ZDPB8PVCO7QGYDQG",
            udid: "ZDPB8PVCO7QGYDQG",
            avatarUri: "mock/avatar.png",
            osName: "ANDROID17(4.2.2)", nodeUri: 'http://172.0.0.1:8080/', nodeName: 'Windows8'
        }, {
            name: "xiaomi-2013022-ZDPB8PVCO7QGYDQG",
            udid: "ZDPB8PVCO7QGYDQG",
            avatarUri: "mock/avatar.png",
            osName: "ANDROID17(4.2.2)", nodeUri: 'http://172.0.0.1:8080/', nodeName: 'Windows8'
        },
        {
            name: "xiaomi-2013022-ZDPB8PVCO7QGYDQG",
            udid: "ZDPB8PVCO7QGYDQG",
            avatarUri: "mock/avatar.png",
            osName: "ANDROID17(4.2.2)", nodeUri: 'http://172.0.0.1:8080/', nodeName: 'Windows8'
        }];
};

Device.prototype.add = function (node) {
    var od = _.find(this.devices, function (d) {
        return d.udid === node.udid;
    });
    if (od) {
        _.extend(od, node);
    } else {
        this.devices.push(node);
    }
};

Device.prototype.toArray = function () {
    return this.devices;
};

module.exports = Device;
