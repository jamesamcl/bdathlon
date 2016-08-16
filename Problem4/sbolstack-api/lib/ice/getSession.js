
const config = require('config')

const request = require('request')

const sessions = {}


function getSession(url, callback) {

    if(sessions[url] !== undefined) {
        callback(null, sessions[url])
    }

    const registries = config.get('iceRegistries')

    const registryInfo = registries.filter((registry) => {

        return registry.url === url

    })[0]

    if(registryInfo === undefined) {

        callback(new Error('ice registry ' + url + ' not in config'))
        return

    }

    request({
        url: url + '/rest/accesstokens',
        method: 'POST',
        json: true,
        followAllRedirects: true,
        body: {
            email: registryInfo.email,
            password: registryInfo.password
        }
    }, (err, res, body) => {

        if(err) {

            callback(err)
            return
        }

        sessions[url] = body.sessionId

        callback(null, body.sessionId)

    })


}

module.exports = getSession

