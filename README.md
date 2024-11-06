

## Graphql
http://localhost:8000/graphiql?path=/graphql

```graphql
query {
  hello
  greeting(name: "Test")
}
```


we only need the `resource/graphql/scheam.graphqls` file to define the schema and the `resource/graphql/resolver.kt` file to define the resolvers.
and then the voayger and the doc would work as expected.