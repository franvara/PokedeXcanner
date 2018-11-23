package es.vass.pokedexcanner.pokemonList.list

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.design.widget.Snackbar
import es.vass.pokedexcanner.Injector
import es.vass.pokedexcanner.pokemonList.pokemonDetail.PokemonDetailActivity
import es.vass.pokedexcanner.R
import es.vass.pokedexcanner.app.pokemonList.PokemonListAdapter
import es.vass.pokedexcanner.app.pokemonList.PokemonListViewModel

import kotlinx.android.synthetic.main.activity_pokemon_list.*
import kotlinx.android.synthetic.main.pokemon_list.*

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [PokemonDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class PokemonListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    private lateinit var pokemonListViewModel: PokemonListViewModel
    private lateinit var rvAdapter: PokemonListAdapter

    var pokeid: Long = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        fab.setOnClickListener { view ->
            pokemonListViewModel.viewPokemon(pokeid++)
        }

        if (pokemon_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        setupViewModel()
        setupRecyclerView(pokemon_list)
        observeViewModel()
    }

    fun setupViewModel() {
        pokemonListViewModel = ViewModelProviders.of(this, Injector.providePokemonListViewModelFactory(this))
            .get(PokemonListViewModel::class.java)
    }


    private fun setupRecyclerView(recyclerView: RecyclerView) {
        rvAdapter = PokemonListAdapter(this, twoPane)
        recyclerView.adapter = rvAdapter
    }

    fun observeViewModel() {
        pokemonListViewModel.pokemonList.observe(this, Observer { pokemonList ->
            pokemonList?.let {
                rvAdapter.pokemonList = pokemonList
                rvAdapter.notifyDataSetChanged()
            }
        })
    }

    /*class SimpleItemRecyclerViewAdapter(
        private val parentActivity: PokemonListActivity,
        private val values: List<DummyContent.DummyItem>,
        private val twoPane: Boolean
    ) :
        RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as DummyContent.DummyItem
                if (twoPane) {
                    val fragment = PokemonDetailFragment().apply {
                        arguments = Bundle().apply {
                            putString(PokemonDetailFragment.ARG_ITEM_ID, item.id)
                        }
                    }
                    parentActivity.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.pokemon_detail_container, fragment)
                        .commit()
                } else {
                    val intent = Intent(v.context, PokemonDetailActivity::class.java).apply {
                        putExtra(PokemonDetailFragment.ARG_ITEM_ID, item.id)
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.pokemon_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.idView.text = item.id
            holder.contentView.text = item.content

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val idView: TextView = view.id_text
            val contentView: TextView = view.content
        }
    }*/
}
