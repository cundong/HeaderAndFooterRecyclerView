## HeaderAndFooterRecyclerView

## Introduction

HeaderAndFooterRecyclerView is a RecyclerView solution that supports addHeaderView, addFooterView to a RecyclerView.

Through this library, you can implement RecyclerView's Page Loading by dynamically modify the FooterView's State, such as "loading", "loading error", "loading success", "slipping to the bottom".

## How to Use It

* Add HeaderView, FooterView
```java
    mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mDataAdapter);
    mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    //add a HeaderView
    RecyclerViewUtils.setHeaderView(mRecyclerView, new SampleHeader(this));

    //add a FooterView
    RecyclerViewUtils.setFooterView(mRecyclerView, new SampleFooter(this));
```
* LinearLayout / GridLayout / StaggeredGridLayout layout of RecyclerView paging load

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

## Attention

If you have already added a HeaderView for RecyclerView by ```RecyclerViewUtils.setHeaderView(mRecyclerView, view);``` , then call the ViewHolder 's ```getAdapterPosition()```、```getLayoutPosition()```, ,the returned value will be affected by the addition of the HeaderView (the return position is the real position + headerCounter).

Therefore, in this case, please use: ```RecyclerViewUtils.getAdapterPosition(mRecyclerView, ViewHolder.this)```, ```RecyclerViewUtils.getLayoutPosition(mRecyclerView, ViewHolder.this)```.

## Demo

* Add HeaderView, FooterView

![Screenshots][1]

* Support for ply loading of the LinearLayout layout RecyclerView

![Screenshots][2]

* Support for paging loads of GridLayout layout RecyclerView

![Screenshots][3]

* Supports paging loads of StaggeredGridLayout layout RecyclerView

![Screenshots][4]

* The page load fails when the GridLayout layout is RecyclerView

![Screenshots][5]


## License

> Copyright 2015 Cundong
> 
> Licensed under the Apache License, Version 2.0 (the "License"); you
> may not use this file except in compliance with the License. You may
> obtain a copy of the License at
> 
>    http://www.apache.org/licenses/LICENSE-2.0
> 
> Unless required by applicable law or agreed to in writing, software
> distributed under the License is distributed on an "AS IS" BASIS,
> WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
> implied. See the License for the specific language governing
> permissions and limitations under the License.

[1]: https://raw.githubusercontent.com/cundong/HeaderAndFooterRecyclerView/master/art/art1.png
[2]: https://raw.githubusercontent.com/cundong/HeaderAndFooterRecyclerView/master/art/art2.png
[3]: https://raw.githubusercontent.com/cundong/HeaderAndFooterRecyclerView/master/art/art3.png
[4]: https://raw.githubusercontent.com/cundong/HeaderAndFooterRecyclerView/master/art/art4.png
[5]: https://raw.githubusercontent.com/cundong/HeaderAndFooterRecyclerView/master/art/art5.png
