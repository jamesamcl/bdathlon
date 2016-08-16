
function collateMetadata(metaLists, res) {

    var foundURIs = {}
    var list = []


    console.log('collating ' + metaLists.length + ' metalists')

    console.log(JSON.stringify(metaLists))

    metaLists.forEach((metaList) => {

        JSON.parse(metaList.body).forEach((meta) => {

            if(foundURIs[meta.uri] === undefined) {

                list.push(meta)
                foundURIs[meta.uri] = true

            }

        })

    })

    res.header('content-type', 'application/json')
    res.send(JSON.stringify(list, null, 2))
}

module.exports = collateMetadata

