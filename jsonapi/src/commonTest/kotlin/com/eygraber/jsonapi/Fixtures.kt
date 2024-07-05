package com.eygraber.jsonapi

val SingleResourceJson = """
{
    "data": {
        "id": "1",
        "type": "articles",
        "attributes": {
            "title": "JSON:API paints my bikeshed!"
        },
        "relationships": {
            "author": {
                "links": {
                    "self": "http://example.com/people/9"
                },
                "data": {
                    "id": "9",
                    "type": "people"
                },
                "meta": {
                    "created": "2019-01-01T00:00:00Z"
                }
            },
            "comments": {
                "data": [
                    {
                        "id": "5",
                        "type": "comments"
                    },
                    {
                        "id": "12",
                        "type": "comments"
                    }
                ]
            }
        },
        "links": {
            "self": "http://example.com/articles/1"
        },
        "meta": {
            "created": "2019-01-01T00:00:00Z"
        }
    },
    "meta": {
        "created": "2019-01-01T00:00:00Z"
    },
    "jsonapi": {
        "version": "1.1",
        "meta": {
            "created": "2019-01-01T00:00:00Z"
        }
    },
    "links": {
        "self": "http://example.com/articles/1",
        "related": {
            "href": "http://example.com/comments/5"
        },
        "random": {
            "href": "http://example.com",
            "title": "Random title"
        }
    },
    "included": [
        {
            "id": "9",
            "type": "people",
            "attributes": {
                "firstName": "Dan",
                "lastName": "Gebhardt",
                "twitter": "dgeb"
            },
            "links": {
                "self": "http://example.com/people/9"
            }
        },
        {
            "id": "5",
            "type": "comments",
            "attributes": {
                "body": "First!"
            }
        },
        {
            "id": "12",
            "type": "comments",
            "attributes": {
                "body": "I like XML better"
            }
        }
    ]
}
""".trim()

val MultipleResourcesJson = """
{
    "data": [
        {
            "id": "1",
            "type": "articles",
            "attributes": {
                "title": "JSON:API paints my bikeshed!"
            },
            "relationships": {
                "author": {
                    "data": {
                        "id": "9",
                        "type": "people"
                    }
                },
                "comments": {
                    "data": [
                        {
                            "id": "5",
                            "type": "comments"
                        },
                        {
                            "id": "12",
                            "type": "comments"
                        }
                    ]
                }
            }
        },
        {
            "id": "2",
            "type": "articles",
            "attributes": {
                "title": "Rails is Omakase"
            },
            "relationships": {
                "author": {
                    "data": {
                        "id": "9",
                        "type": "people"
                    }
                },
                "comments": {
                    "data": [
                        {
                            "id": "3",
                            "type": "comments"
                        },
                        {
                            "id": "4",
                            "type": "comments"
                        }
                    ]
                }
            }
        }
    ],
    "included": [
        {
            "id": "9",
            "type": "people",
            "attributes": {
                "firstName": "Dan",
                "lastName": "Gebhardt",
                "twitter": "dgeb"
            }
        },
        {
            "id": "5",
            "type": "comments",
            "attributes": {
                "body": "First!"
            }
        },
        {
            "id": "12",
            "type": "comments",
            "attributes": {
                "body": "I like XML better"
            }
        }
    ]
}
""".trim()

val SingleResourceIdentifierJson = """
{
    "data": {
        "id": "1",
        "type": "articles",
        "meta": {
            "created": "2019-01-01T00:00:00Z"
        }
    }
}
""".trim()

val MultipleResourceIdentifiersJson = """
{
    "data": [
        {
            "id": "1",
            "type": "articles"
        },
        {
            "id": "2",
            "type": "articles"
        }
    ]
}
""".trim()

val MetaOnlyJson = """
{
    "meta": {
        "foo": "bar",
        "baz": "bang"
    },
    "links": {
        "self": "http://example.com/foo/bar/baz/bang"
    }
}
""".trim()

val ErrorsPointerJson = """
{
    "errors": [
        {
            "status": "422",
            "title": "Invalid Attribute",
            "detail": "Title is required",
            "source": {
                "pointer": "/data/attributes/title"
            }
        }
    ]
}
""".trim()

val ErrorsParameterJson = """
{
    "errors": [
        {
            "status": "422",
            "title": "Invalid Attribute",
            "detail": "Title is required",
            "source": {
                "parameter": "title"
            }
        }
    ]
}
""".trim()

val ErrorsHeaderJson = """
{
    "errors": [
        {
            "status": "422",
            "title": "Invalid Attribute",
            "detail": "Title is required",
            "source": {
                "header": "title"
            }
        }
    ]
}
""".trim()

val ErrorsWithPointerAndParameterJson = """
{
    "errors": [
        {
            "status": "422",
            "title": "Invalid Attribute",
            "detail": "Title is required",
            "source": {
                "pointer": "/data/attributes/title",
                "parameter": "title"
            }
        }
    ]
}
""".trim()

val InvalidDocumentDataAndErrors = """
{
    "data": {
        "id": "1",
        "type": "articles"
    },
    "errors": [
        {
            "status": "422",
            "title": "Invalid Attribute",
            "detail": "Title is required",
            "source": {
                "pointer": "/data/attributes/title",
                "parameter": "title"
            }
        }
    ]
}
""".trim()

val InvalidDocumentIncludedResourcesWithoutData = """
{
    "errors": [
        {
            "status": "422",
            "title": "Invalid Attribute",
            "detail": "Title is required",
            "source": {
                "pointer": "/data/attributes/title",
                "parameter": "title"
            }
        }
    ],
    "included": [
        {
            "id": "9",
            "type": "people",
            "attributes": {
                "firstName": "Dan",
                "lastName": "Gebhardt",
                "twitter": "dgeb"
            }
        }
   ]
}
""".trim()
