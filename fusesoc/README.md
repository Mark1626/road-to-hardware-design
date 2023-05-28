# Fusesoc based projects

## Building and running

To setup the project

```sh
cd <example>
fusesoc library add fusesoc_cores https://github.com/fusesoc/fusesoc-cores
fusesoc library add <example> $PWD
fusesoc run --target=alchitry-cu <example>
```

