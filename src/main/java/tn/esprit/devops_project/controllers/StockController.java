@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@ActiveProfiles("test")
class StockServiceImplTest {

    @Autowired
    private StockServiceImpl stockService;

    @Test
    void addStock() {
    }

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    void retrieveStock() {
        final Stock stock = this.stockService.retrieveStock(1L);
        assertEquals("stock 1", stock.getTitle());
    }

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    void retrieveAllStock() {
        final List<Stock> allStocks = this.stockService.retrieveAllStock();
        if (!CollectionUtils.isEmpty(allStocks)) {
            assertEquals(allStocks.size(), 1);
        }
    }
}