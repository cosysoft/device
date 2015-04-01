/**
 * Created by 兰天 on 2015/3/30.
 */
var _ = require('underscore');

var Device = function () {
    this.devices = [{
        nodeIp: '172.0.0.1', nodeName: 'Windows8', udid: '1111111111'
    }, {
        nodeIp: '172.0.0.1', nodeName: 'Windows8', udid: '2222222222222'
    }, {
        nodeIp: '172.0.0.1', nodeName: 'Windows8', udid: '2222222222222'
    }, {
        nodeIp: '172.0.0.1', nodeName: 'Windows8', udid: '2222222222222'
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
