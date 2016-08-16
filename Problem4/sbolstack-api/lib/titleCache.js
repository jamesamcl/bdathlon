

function rebuildCache() {

    var query = [
        'SELECT DISTINCT ?subject ?title WHERE {',
        '?subject dcterms:title ?title',
        '}'
    ].join('\n')

    repo.sparql(query, 'application/json', (err, type, result) => {



    })



}

module.exports = {

    rebuildCache: rebuildCache

}

