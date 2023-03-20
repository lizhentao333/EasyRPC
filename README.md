# EasyRPC
## 搭建一个简易RPC框架

### 模块划分

+ register：用来和注册中心交互，包括注册服务、订阅服务、服务变更通知等功能。

+ protocol：用来进行 RPC 服务的描述和 自定义通信协议。

+ serialize：将 RPC 请求中的参数、结果等对象进行序列化与反序列化

+ transport：用来进行远程通信，默认使用 Netty NIO 的 TCP 长链接方式。

+ cluster：请求时会根据不同的高可用与负载均衡策略选择一个可用的 Server 发起远程调用。
+ common：用于存放一些常量、数据包类型、异常类型等通用数据



## 通信流程
### 服务端
+ 首先向注册中心注册自身服务，并在自身存储这些服务，方法快速处理请求，将服务注册完成后，自身就一直监听socket，等待客户端的请求
+ 当收到客户端请求时，首先核对报文头，然后根据其对应的序列化方法进行反序列化。
+ 在获取到对应的请求报文数据后，根据其指定的方法名和方法参数，使用反射调用自身对应方法
+ 将方法结果封装到响应报文中，然后对其进行编码，（加上响应报文头，并对返回结果进行序列化）
+ 最后将报文发送给客户端。

### 客户端

+ 使用代理对象调用对应的服务，首先向注册中心请求所需服务的(ip:port)，当有多个服务提供者时，这边会根据对应的负载均衡算法选择合适的
服务端调用。
+ 首先获取一条用于通信的channel，然后向此通道写入请求数据
+ 首先将其序列化，然后封装到请求报文中，最后将其发送给对应的服务端
+ 服务端在处理对应的请求后，将响应结果返回给对应客户端
+ 客户端在收到响应结果后，首先对响应报文进行解码，获取响应数据
+ 之后又对应的代理对象将此结果返回给客户。

### 注册中心
+ 注册中心主要用于存储服务对应的服务器地址，用于服务端注册和客户端获取服务这两个操作
+ 在ZooKeeper中，服务端注册临时节点，这样在服务端关闭后就自然地将其注册的所有服务从注册中心中删除，这样就可以避免客户端访问到下线的服务端
+ 并且在每个节点下添加监听器，当某个服务变动时，会通知对应的服务器，并更新自身的服务表
+ 当同一个服务存在多个提供商时，可以根据相应的负载均衡算法选择一个合适的地址(ip:port)返回给客户端。
