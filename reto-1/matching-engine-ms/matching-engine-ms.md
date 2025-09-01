classDiagram
direction BT
class MatchEngineController {
  + MatchEngineController(MatchEngineManager) 
  - MatchEngineManager matchEngine
  - Logger log
  + addOrder(CreateOrderRequest) Mono~ResponseEntity~GenericResponse~String~~~
  + getOrdersBook() Mono~ResponseEntity~GenericResponse~OrderBookDTO~~~
  + cancelOrder(int) Mono~ResponseEntity~GenericResponse~String~~~
  + getQuotaSummary() Mono~ResponseEntity~GenericResponse~QuotaSummaryDTO~~~
  + updateOrder(UpdateOrderRequest) Mono~ResponseEntity~GenericResponse~String~~~
}
class MatchEngineManager {
  + MatchEngineManager(OrderUseCase) 
  - Logger log
  - OrderUseCase orderUseCase
  - AtomicInteger counter
  + getBestBuyAndSell() Mono~QuotaSummaryDTO~
  + updateOrder(UpdateOrderRequest) Mono~Void~
  + cancelOrder(int) Mono~Void~
  + addOrder(CreateOrderRequest) Mono~Void~
  + getOrderBook() Mono~OrderBookDTO~
}
class Order {
  + Order(int, OrderType, LocalDateTime, double, int) 
  + Order(int, OrderType, double, int) 
  + Order(Order) 
  - LocalDateTime entryTime
  - int id
  - double price
  - OrderType orderType
  - int quantity
  + getQuantity() int
  + getEntryTime() LocalDateTime
  + toString() String
  + hashCode() int
  + setPrice(double) void
  + equals(Object) boolean
  + getPrice() double
  + toBuilder() OrderBuilder
  + getOrderType() OrderType
  + getId() int
  + builder() OrderBuilder
  + setQuantity(int) void
}
class OrderAdapter {
  + OrderAdapter(ObjectMapper, Timer) 
  - NavigableMap~Double, PriceLevelPQ~ buyPriceLevels
  - Timer matchOrderTimer
  - ObjectMapper objectMapper
  - Logger log
  - Map~Integer, Order~ orderCache
  - NavigableMap~Double, PriceLevelPQ~ sellPriceLevels
  + cancelOrder(int) Mono~Void~
  - removeOrder(NavigableMap~Double, PriceLevelPQ~, Order) Mono~Void~
  - removeAndAddOrder(Order, Order) Mono~Void~
  + addOrder(Order) Mono~Void~
  - matchOrders() Mono~Void~
  - removeOrder(Order) Mono~Void~
  + printBestBuyAndSell() Mono~QuotaSummary~
  + printOrderBook() Mono~OrderBook~
  + updateOrder(Order) Mono~Void~
  - addOrderAndMatch(Order) Mono~Void~
  - getOrderItemsFromMap(NavigableMap~Double, PriceLevelPQ~) List~OrderItem~
}
class OrderBook {
  + OrderBook(List~OrderItem~, List~OrderItem~) 
  - List~OrderItem~ buyOrders
  - List~OrderItem~ sellOrders
  + toBuilder() OrderBookBuilder
  + getSellOrders() List~OrderItem~
  + setSellOrders(List~OrderItem~) void
  + setBuyOrders(List~OrderItem~) void
  + getBuyOrders() List~OrderItem~
  + builder() OrderBookBuilder
}
class OrderGateway {
<<Interface>>
  + updateOrder(Order) Mono~Void~
  + printBestBuyAndSell() Mono~QuotaSummary~
  + cancelOrder(int) Mono~Void~
  + addOrder(Order) Mono~Void~
  + printOrderBook() Mono~OrderBook~
}
class OrderItem {
  + OrderItem(double, int, int, List~Order~) 
  - double price
  - int totalVolumeActions
  - int totalOrders
  - List~Order~ orders
  + setTotalOrders(int) void
  + builder() OrderItemBuilder
  + addOrder(Order) void
  + toBuilder() OrderItemBuilder
  + setTotalVolumeActions(int) void
  + getTotalVolumeActions() int
  + getOrders() List~Order~
  + setPrice(double) void
  + getPrice() double
  + getTotalOrders() int
  + setOrders(List~Order~) void
}
class OrderType {
<<enumeration>>
  - OrderType() 
  +  BUY
  +  SELL
  + valueOf(String) OrderType
  + values() OrderType[]
}
class OrderUseCase {
  + OrderUseCase(OrderGateway) 
  - OrderGateway orderGateway
  + cancelOrder(int) Mono~Void~
  + getBestBuyAndSell() Mono~QuotaSummary~
  + getOrderBook() Mono~OrderBook~
  + addOrder(Order) Mono~Void~
  + updateOrder(Order) Mono~Void~
}
class PriceLevelPQ {
  + PriceLevelPQ(double) 
  - PriorityQueue~Order~ orders
  - int totalVolume
  - double price
  + addOrder(Order) void
  + getLevel1() String
  + getOrders() PriorityQueue~Order~
  + removeOrder(Order) void
  + toString() String
  + totalOrders() int
  + hashCode() int
  + equals(Object) boolean
  + isEmpty() boolean
  + getTotalVolume() int
  + pollBestOrder() Order
  + getPrice() double
}

MatchEngineController "1" *--> "matchEngine 1" MatchEngineManager 
MatchEngineManager "1" *--> "orderUseCase 1" OrderUseCase 
Order "1" *--> "orderType 1" OrderType 
OrderAdapter "1" *--> "orderCache *" Order 
OrderAdapter  ..>  OrderGateway 
OrderAdapter "1" *--> "buyPriceLevels *" PriceLevelPQ 
OrderBook "1" *--> "buyOrders *" OrderItem 
OrderItem "1" *--> "orders *" Order 
OrderUseCase "1" *--> "orderGateway 1" OrderGateway 
