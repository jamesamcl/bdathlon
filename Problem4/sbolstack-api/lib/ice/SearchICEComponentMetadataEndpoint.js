
const config = require('config')

const async = require('async')

const getSession = require('./getSession')

const request = require('request')

function searchICEComponentMetadata(req, callback) {

    var params

    if(req.body) {
        params = req.body
    } else {
        params = req.query
    }

    const registries = config.get('iceRegistries')

    const results = []

    async.eachSeries(registries, (registryInfo, next) => {

        const url = registryInfo.url

        getSession(url, (err, sessionId) => {

            if(err) {

                callback(err)
                return

            }

            const qs = {

                q: '',
                offset: 0,
                limit: 15,
                sort: 'relevance',
                asc: true,
                searchWeb: true

            }

            const criteria = params.criteria || []

            criteria.forEach((criterium) => {

                if(criterium.key === 'name') {

                    qs.q = criterium.value

                }

            })

            if(params.offset !== undefined && params.offset !== null)
                qs.offset = parseInt(params.offset)

            if(params.limit !== undefined && params.limit !== null)
                qs.limit = parseInt(params.limit)
            
            request({
                url: url + '/rest/search',
                method: 'GET',
                json: true,
                followAllRedirects: true,
                qs: qs,
                headers: {
                    'X-ICE-Authentication-SessionId': sessionId
                }
            }, (err, res, body) => {

                if(err) {
                    callback(err)
                    return
                }

                Array.prototype.push.apply(results, body.results.map((result) => {

                    const entryInfo = result.entryInfo

                    return {
                        uri: url + '/entry/' + entryInfo.id,
                        name: entryInfo.name,
                        description: entryInfo.shortDescription,
                        displayId: entryInfo.name,
                        version: '1'
                    }
                                                                    
                }))

                next()
            })


        })

    }, (err) => {

        console.log('ice: got ' + results.length + ' results')

        callback(null, 200, {
            mimeType: 'application/json',
            body: JSON.stringify(results)
        })


    })





}

module.exports = searchICEComponentMetadata

