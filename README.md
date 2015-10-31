# HeaderAndFooterRecyclerView

HeaderAndFooterRecyclerView 可以对 RecyclerView 控件进行拓展，简单的给的RecyclerView增加HeaderView、FooterView，并且不需要修改你的Adapter。

同时，还通过增加、更新、移除FooterView实现了RecyclerView的上拉刷新数据时的loading../theEnd等效果。

## 实现原理

实现一个OuterAdapter，继承自RecyclerView.Adapter，把真正的业务Adapter（innerAdapter）传进来，在OuterAdapter中插入头和尾，同时，在OuterAdapter中重新为innerAdapter设置数据监听器，当外部数据变化时，通过计算插入HeaderView、FooterView之后的新位置，调用notifyXXXX系方法来通知数据的改变。

## 使用方法

## 运行效果





