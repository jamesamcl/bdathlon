

const config = require('config')

const async = require('async')

const getSession = require('./getSession')

const request = require('request')

const SBOLDocument = require('sboljs')

function retrieveICESBOLEndpoint(req, callback) {

    console.log('endpoint: RetrieveSBOLEndpoint')

    var uri

    if(req.params.prefix) {

        var prefixes = config.get('prefixes')

        var baseUri = prefixes[req.params.prefix]

        if(!baseUri) {
            return callback(new Error('unknown prefix: ' + req.params.prefix))
        }

        uri = baseUri + req.params.uri

    } else {

        uri = req.params.uri

    }

    const registries = config.get('iceRegistries')

    const results = []

    async.eachSeries(registries, (registryInfo, next) => {

        const url = registryInfo.url

        if(uri.indexOf(url) !== 0) {
            next()
            return
        }

        const id = uri.slice(url.length + '/entry/'.length)

        getSession(url, (err, sessionId) => {

            if(err) {

                callback(err)
                return

            }

            request({
                url: url + '/rest/parts/' + id,
                method: 'GET',
                json: true,
                followAllRedirects: true,
                headers: {
                    'X-ICE-Authentication-SessionId': sessionId
                }
            }, (err, res, body) => {

                if(err) {
                    callback(err)
                    return
                }

                request({
                    url: url + '/rest/file/' + id + '/sequence/sbol2',
                    qs: {
                        sid: body.recordId
                    },
                    method: 'GET',
                    json: true,
                    followAllRedirects: true,
                    headers: {
                        'X-ICE-Authentication-SessionId': sessionId
                    }
                }, (err, res, sbolBody) => {

                    if(err) {
                        callback(err)
                        return
                    }

                    callback(null, 200, {
                        mimeType: 'application/rdf+xml',
                        body: sbolBody
                    })


                })

            })

        })

    })
   





}


module.exports = retrieveICESBOLEndpoint


