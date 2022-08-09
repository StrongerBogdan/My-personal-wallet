package com.bogdanmurzin.mypersonalwallet.ui.presenter

import android.util.Log
import android.widget.SearchView
import com.bogdanmurzin.domain.entities.Icon
import com.bogdanmurzin.mypersonalwallet.common.Constants.TAG
import com.epam.elearn.domain.usecases.icons.GetIconsUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class IconsPresenter(private val getIconsUseCase: GetIconsUseCase) {

    private var view: IconView? = null
    private val compositeDisposable = CompositeDisposable()

    private fun loadIcons(query: String) {
        view?.showProgressBar()
        compositeDisposable.add(
            getIconsUseCase.invoke(query)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> //onSuccess
                        view?.hideProgressBar()
                        result.onSuccess {
                            view?.showIcons(it)
                        }
                        result.onFailure {
                            Log.e(TAG, "loadIcons: ${it.message}")
                        }
                    },
                    { t -> //onError
                        view?.hideProgressBar()
                        Log.e(TAG, "loadIcons: ${t.message}")
                    }
                )
        )
    }

    fun search(searchView: SearchView) {
        PublishSubject.create<String> { emitter ->
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (!emitter.isDisposed && !newText.isNullOrEmpty()) {
                        emitter.onNext(newText)
                    }
                    return false
                }
            })
        }
            .debounce(1000, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .filter { text -> text.isNotEmpty() && text.length >= 3 }
            .subscribeOn(Schedulers.io())
            .switchMap { text -> Observable.just(text) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { query ->
                Log.i(TAG, "search: $query")
                loadIcons(query)
            }
    }

    fun attachView(usersView: IconView) {
        view = usersView
    }

    fun detachView() {
        view = null
    }

    fun clearCompositeDisposable() {
        compositeDisposable.clear()
    }

    interface IconView {
        fun showIcons(list: List<Icon>)
        fun showProgressBar()
        fun hideProgressBar()
    }
}