##HeaderAndFooterRecyclerView

------

##介绍

HeaderAndFooterRecyclerView是支持addHeaderView、 addFooterView、分页加载数据的RecyclerView。

它可以对 RecyclerView 控件进行拓展，给RecyclerView增加HeaderView、FooterView，并且不需要对你的Adapter做任何修改。

同时，通过修改FooterView State，可以动态为FooterView赋予不同状态（加载中、加载失败、滑到最底等），可以实现RecyclerView分页加载数据时的loading/theEnd/NetWorkError效果。

##使用

* 添加HeaderView、FooterView
```java
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //add a HeaderView
        RecyclerViewUtils.setHeaderView(mRecyclerView, new SampleHeader(this));

        //add a FooterView
        RecyclerViewUtils.setFooterView(mRecyclerView, new SampleFooter(this));
```

* LinearLayout/GridLayout/StaggeredGridLayout布局的RecyclerView分页加载

```java
mRecyclerView.addOnScrollListener(mOnScrollListener);
```

```java
private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {

        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);

            LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mRecyclerView);
            if(state == LoadingFooter.State.Loading) {
                Log.d("@Cundong", "the state is Loading, just wait..");
                return;
            }

            mCurrentCounter = mDataList.size();

            if (mCurrentCounter < TOTAL_COUNTER) {
                // loading more
                RecyclerViewStateUtils.setFooterViewState(EndlessLinearLayoutActivity.this, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
                requestData();
            } else {
                //the end
                RecyclerViewStateUtils.setFooterViewState(EndlessLinearLayoutActivity.this, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
            }
        }
    };
```
## 注意事项

如果已经使用 ```RecyclerViewUtils.setHeaderView(mRecyclerView, view);``` 为RecyclerView添加了HeaderView，那么再调用ViewHolder类的```getAdapterPosition()```、```getLayoutPosition()```时返回的值就会因为增加了Header而受影响。

因此，这种情况下请使用
```RecyclerViewUtils.getAdapterPosition(mRecyclerView, ViewHolder.this)```、```RecyclerViewUtils.、getLayoutPosition(mRecyclerView, ViewHolder.this)``` 两个方法来替代。

## Demo

* 添加HeaderView、FooterView

![截屏][1]

* 支持分页加载的LinearLayout布局RecyclerView

![截屏][2]

* 支持分页加载的GridLayout布局RecyclerView

![截屏][3]

* 支持分页加载的StaggeredGridLayout布局RecyclerView

![截屏][4]

## 关于我

* Blog: [http://my.oschina.net/liucundong/blog][5]
* Mail: cundong.liu#gmail.com

## License

    Copyright 2015 Cundong

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

  [1]: https://raw.githubusercontent.com/cundong/HeaderAndFooterRecyclerView/master/art/art1.png
  [2]: https://raw.githubusercontent.com/cundong/HeaderAndFooterRecyclerView/master/art/art2.png
  [3]: https://raw.githubusercontent.com/cundong/HeaderAndFooterRecyclerView/master/art/art3.png
  [4]: https://raw.githubusercontent.com/cundong/HeaderAndFooterRecyclerView/master/art/art4.png
  [5]: http://my.oschina.net/liucundong/blog
