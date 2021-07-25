# Developing on Cloud Nine
We all know the advantages of developing locally. It's fast, it's easy, it's free. You can easily find the running apps and their logs, and debugging is a breeze.
But the local development environment is not realistic because dependencies cannot be emulated like they can in the real world. There can be small or large differences from production environments. Additionally, all services have to be set up locally and kept in sync during production. This can potentially be a big burden for developers to handle as applications scale. Some services are impossible to run locally, like various types of data stores and queues provided as managed services by cloud vendors.
Mocking or replicating cloud services or managed services in your local environment simply no longer translates to the real environment upon which your application will run during production. This means you are missing the chance to find potential sources of failures and not testing your applications in a realistic manner.

## Using [Helm](https://helm.sh/) & [Kubectl](https://kubernetes.io/docs/tasks/tools/#kubectl) Directly
### Deploy
* Deploy (in this case to local k8s cluster - docker-desktop): [greeting] `helm upgrade --install greeting ./helm`
* Check rollout status: `kubectl rollout status deployment greeting`
* Browse service: http://localhost:8088/greet/hello

### Develop
* Change greeting in [GreetingController](greeting/src/main/java/com/att/training/k8s/greeting/GreetingController.java) to "Shalom"
* Build the app: [greeting] `./gradlew build -x check`
* Build docker images (1.0.0): [greeting] `docker build -t k8s-demo/greeting:1.0.0 .`
* Deploy and check rollout status
* Tail logs using [kubetail](https://github.com/johanhaleby/kubetail): `kt greeting`  
* Browse service: http://localhost:8088/greet/hello

### Debug
* Use debug flag & reduce # of replicas to 1: [greeting] `helm upgrade --install greeting ./helm --set replicas=1 --set debug=true`
* Run `kubectl rollout status deployment greeting`
* port-forward local port 5005 to pod's 5005: `kubectl greeting-<pod-hash> 5005`
* Launch debugger and put a breakpoint in the [GreetingController](greeting/src/main/java/com/att/training/k8s/greeting/GreetingController.java), and hit http://localhost:8088/greet/hello
* Clean up: revert replicas and debug: `helm upgrade --install --reset-values greeting ./helm`

## [Skaffold](https://skaffold.dev/)
### Develop
* Run [`skaffold dev`](https://skaffold.dev/docs/workflows/dev/)
* Browse http://localhost:8088/greet/hello
* Change greeting in [GreetingController](greeting/src/main/java/com/att/training/k8s/greeting/GreetingController.java) to "Shalom", build app (gbx)
* Wait for rollout and browse http://localhost:8088/greet/hello
* Clean-up: `ctrl-C`

### Debug
* Run [`skaffold debug`](https://skaffold.dev/docs/workflows/debug/)
* This adds the agentlib JVM flag and reduces replicaset to 1
* Put a breakpoint in [GreetingController](greeting/src/main/java/com/att/training/k8s/greeting/GreetingController.java)
* Browse http://localhost:8088/greet/hello?name=Yaniv

## [Telepresence](https://www.telepresence.io/)
### [Connect](https://www.telepresence.io/docs/latest/howtos/outbound/)
* Deploy greeting & reverse with helm
* Check rollout status
* Present the reverse-service by browsing http://localhost:8088/greet/hello?name=World&reversed=true
* Run `kubectl get svc reverse-service` and show that this is a ClusterIP service, inaccessible from outside
* `curl http://reverse-service.default/reverse/hello` fails
* Run `telepresence connect`
* Run `telepresence status`
* Telepresence allows us to access the services as if we're in the cluster: `curl http://reverse-service.default/reverse/hello` now succeeds. We need to add the `.<namespace>`
to the service name when we're not inside a pod that belongs to a specific namespace.
* Note: we can add any pod into our namespace and get the kube dns: 
  * `kubectl run curl -it --image=radial/busyboxplus:curl`
  *  In the shell, run: `curl http://reverse-service/reverse/hello`
  *  How do we get the DNS suffix? Run `cat /etc/resolv.conf`
  * Clean-up: `ctrl-c` and then run `kubectl delete pod curl`

### [Intercept](https://www.telepresence.io/docs/latest/reference/intercepts/)
* Run `telepresence intercept reverse --port 8090 --env-file=./reverse.env`
* Run reverse app locally on port 8090 (configure the app with the EnvFile plugin to pick up ./reverse.env)
* Now we don't need to qualify the service name with the namespace: `curl http://reverse-service/reverse/hello`
* Browse http://localhost:8088/greet/hello?name=Yaniv&reversed=true
* Edit [ReverseController](reverse/src/main/java/com/att/training/k8s/reverse/ReverseController.java): add `.append("***")`
 and re-run. Refresh browser and see changes.
* Debug reverse: put a breakpoint in [ReverseController](reverse/src/main/java/com/att/training/k8s/reverse/ReverseController.java)
  and refresh the browser. You'll hit the breakpoint.

### Cleanup
* `telepresence leave reverse`
* Refresh the browser (no "***")
* To remove all Telepresence component (`kubectl get all -n ambassador`): Run `telepresence uninstall --everything`
* `telepresence quit`
