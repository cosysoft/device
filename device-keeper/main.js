/**
 * Created by 兰天 on 2015/3/30.
 */
var express = require('express');
var Device = require('./keeper/device');

var app = express();
var device = new Device();


app.use(express.static('app'));

app.get('/api', function (req, res) {
    res.send('Hello World!')
})
app.get('/hub/device', function (req, res) {
    res.json(device.toArray());
});

var server = app.listen(3000, function () {

    var host = server.address().address
    var port = server.address().port

    console.log('Example app listening at http://%s:%s', host, port)

    console.log('http://localhost:3000/static/');
});