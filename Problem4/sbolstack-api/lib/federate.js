
var config = require('config')

var async = require('async')

var request = require('request')

function FederationMiddleware(endpoints, collate) {

    var otherStacks = config.get('otherStacks')

    endpoints = Array.isArray(endpoints) ? endpoints : [ endpoints ]

    console.log(endpoints.length + ' local endpoints')

    return function federate(req, res) {

        var responses = []

        async.eachSeries(endpoints, queryLocalEndpoint, onLocalEndpointsFinished)

        function queryLocalEndpoint(endpoint, next) {

            endpoint(req, (err, statusCode, localResponse) => {

                if(err) {

                    return res.status(500).send(err.toString())

                }

                if(statusCode >= 300) {

                    if(statusCode !== 404) {

                        return res.status(statusCode).send(localResponse)

                    }

                } else {

                    responses.push(localResponse)

                }

                next()
                    
            })

        }

        function onLocalEndpointsFinished(err) {

            async.eachSeries(otherStacks, queryRemoteStack, onFederationComplete)

        }

        function queryRemoteStack(otherStackUrl, next) {

            console.log('[Federation] Querying remote stack: ' + otherStackUrl)

            request({

                method: req.method,
                uri: otherStackUrl + req.url

            }, (err, remoteResponse) => {

                console.log('[Federation] Received response')

                if(remoteResponse.statusCode >= 300) {

                    if(remoteResponse.statusCode === 404) {
                        next()
                    } else {
                        res.status(remoteResponse.statusCode).send(remoteResponse)
                    }

                } else {

                    responses.push(remoteResponse)
                    next()

                }
            })

        }

        function onFederationComplete(err) {

            if(err) {
                return res.status(500).send(err)
            }

            collate(responses, res)

        }
    }
}

module.exports = FederationMiddleware

