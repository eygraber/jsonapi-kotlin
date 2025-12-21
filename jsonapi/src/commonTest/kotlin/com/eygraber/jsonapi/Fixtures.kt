@file:Suppress("TrimMultilineRawString")

package com.eygraber.jsonapi

val SingleResourceJson = """
{
    "data": {
        "type": "articles",
        "id": "1",
        "attributes": {
            "title": "JSON:API paints my bikeshed!"
        },
        "relationships": {
            "author": {
                "links": {
                    "self": "http://example.com/people/9"
                },
                "data": {
                    "type": "people",
                    "id": "9"
                },
                "meta": {
                    "created": "2019-01-01T00:00:00Z"
                }
            },
            "comments": {
                "data": [
                    {
                        "type": "comments",
                        "id": "5"
                    },
                    {
                        "type": "comments",
                        "id": "12"
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
            "type": "people",
            "id": "9",
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
            "type": "comments",
            "id": "5",
            "attributes": {
                "body": "First!"
            }
        },
        {
            "type": "comments",
            "id": "12",
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
            "type": "articles",
            "id": "1",
            "attributes": {
                "title": "JSON:API paints my bikeshed!"
            },
            "relationships": {
                "author": {
                    "data": {
                        "type": "people",
                        "id": "9"
                    }
                },
                "comments": {
                    "data": [
                        {
                            "type": "comments",
                            "id": "5"
                        },
                        {
                            "type": "comments",
                            "id": "12"
                        }
                    ]
                }
            }
        },
        {
            "type": "articles",
            "id": "2",
            "attributes": {
                "title": "Rails is Omakase"
            },
            "relationships": {
                "author": {
                    "data": {
                        "type": "people",
                        "id": "9"
                    }
                },
                "comments": {
                    "data": [
                        {
                            "type": "comments",
                            "id": "3"
                        },
                        {
                            "type": "comments",
                            "id": "4"
                        }
                    ]
                }
            }
        }
    ],
    "included": [
        {
            "type": "people",
            "id": "9",
            "attributes": {
                "firstName": "Dan",
                "lastName": "Gebhardt",
                "twitter": "dgeb"
            }
        },
        {
            "type": "comments",
            "id": "5",
            "attributes": {
                "body": "First!"
            }
        },
        {
            "type": "comments",
            "id": "12",
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
        "type": "articles",
        "id": "1",
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
            "type": "articles",
            "id": "1"
        },
        {
            "type": "articles",
            "id": "2"
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

val ErrorsWithMetaJson = """
{
    "errors": [
        {
            "status": "422",
            "title": "Invalid Attribute",
            "detail": "Title is required",
            "source": {
                "pointer": "/data/attributes/title"
            },
            "meta": {
                "created": "2019-01-01T00:00:00Z"
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
        "type": "articles",
        "id": "1"
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
            "type": "people",
            "id": "9",
            "attributes": {
                "firstName": "Dan",
                "lastName": "Gebhardt",
                "twitter": "dgeb"
            }
        }
   ]
}
""".trim()
