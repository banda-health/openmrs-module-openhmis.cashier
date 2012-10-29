describe("PaginateView", function() {
	it("requires PaginateView", function() { require({ 'view/paginate': 'view/paginate' })});
	
	it("should determine the number of pages to display", function() {
		var list = new openhmis.GenericCollection();
		list.totalLength = 30;
		var paginateView = new openhmis.PaginateView({
			model: list,
			numberOfPages: 5,
			pageSize: 10
		});

		var pageRange = paginateView.getPageRange();
		expect(pageRange.first).toEqual(1);
		expect(pageRange.last).toEqual(3);
		
		paginateView.page = 10;
		list.totalLength = 95;
		var pageRange = paginateView.getPageRange();
		expect(pageRange.first).toEqual(6);
		expect(pageRange.last).toEqual(10);
	});
});