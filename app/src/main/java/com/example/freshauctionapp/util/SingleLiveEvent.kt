package com.example.subway.util

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/*  LiveData를 이용하다보면, View의 재활성화 (start나 resume 상태로 재진입)가 되면서
   LiveData가 observe를 호출하여, 불필요한 Observer Event까지 일어나는 경우가 있습니다.
   이를 방지하기 위해 기존 LiveData를 상속하여 만들어낸 것이 SingleLiveEvent입니다.

  - 즉 postValue나 setValue, call등을 통하여 setValue 함수를 거쳐야만이
    Observer을 통하여 데이터를 전달 할 수 있으며,
    이는 Configuration Changed 등의 LifeCycleOwner의 재활성화 상태가 와도 원치 않는 이벤트가
    일어나는 것을 방지할 수 있도록 해줍니다.

     SingleLiveEvent는 데이터가 한번 만 출력
     프래그먼트는 재사용성 때문에 메모리에서 완전히 삭제하지 않는다.
     따라서 이전에 사용했던 뮤터블 라이브 데이터가 완전히 클리어 되지 않는 문제가 있다.

     예를 들어  네트워크 상에서 어떤 과일을 검색하고, 다시 다른 과일을 검색했을 때 해당 과일이 없는 경우,
     이전 데이터를 가지고 있기 때문에, 이전 데이터가 화면에 출력하는 문제가 발생할 수 있음

     데이터를 한번 만 발행하고 다시는 그 데이터가 옵저버 되지 않도록 하기 위해 SingleLiveEvent 사용
     즉 옵저버를 딱 한번 만 해주기 위해 사용
     주로 네트워크를 요청해서 그 결과를 화면에 출력할 때 사용
 */

class SingleLiveEvent<T> : MutableLiveData<T>() {

    /*  isPending 변수는 setValue로 새로운 이벤트를 받으면 true로 바뀌고
        그 이벤트가 실행되면 false로 돌아갑니다.
        - 멀티쓰레딩 환경에서 동시성을 보장하는 AtomicBoolean.
        - 초기값은 false로 초기화
     */
     private val isPending = AtomicBoolean(false)

    /* View(Activity or Fragment 등 LifeCycleOwner)가 활성화 상태가 되거나
        setValue로 값이 바뀌었을 때 호출되는 observe 함수.
       - 내부에 등록된 Observer는 isPending이 true인지 확인하고,
         true일 경우 다시 false로 돌려 놓은 후에 이벤트가 호출되었다고 알립니다.
     */
    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {

        //observer가 여러개 들어와도 오직 1개만 처리하고자 하는 매커니즘
        super.observe(owner, Observer<T> { t ->
            /* isPending변수가 true면 if문 내의 로직을 처리하고 false로 바꾼다
               - 아래의 setValue를 통해서만 pending값이 true로 바뀌기 때문에,
                 Configuration Changed가 일어나도 pending값은 false이기 때문에
                 observe가 데이터를 전달하지 않음      */
            if (isPending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }


    /* 1. 값이 변경되면 false였던 isPending이 true로 바뀌고, Observer가 호출
        - compareAndSet과 setValue의 true로 바꿔주는 작업을 통해,
          setValue를 한 번 하면 observer의 코드도 단 한번만 수행
        - 이렇게 setValue를 한 번 했다면 observer는 두 번할 수 없게 함으로서,
          '단 한번의 이벤트에 대한 Observing'을 구현
     */
    @MainThread
    override fun setValue(t: T?) {
        isPending.set(true)
        super.setValue(t)
    }

    /* 데이터의 속성을 지정해주지 않아도 call만으로 setValue를 호출 가능
     * 이미 세팅된 값이라도 call()를 사용하면 null값을 방출합니다.
     * (변수를 비웁니다.)
     */
    @MainThread
    fun call() {
        value = null
    }
}
